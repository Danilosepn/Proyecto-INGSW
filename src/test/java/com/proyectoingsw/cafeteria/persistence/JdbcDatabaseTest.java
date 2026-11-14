package com.proyectoingsw.cafeteria.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.proyectoingsw.cafeteria.application.DefaultGestorTicket;
import com.proyectoingsw.cafeteria.domain.Alimento;
import com.proyectoingsw.cafeteria.domain.Factura;
import com.proyectoingsw.cafeteria.domain.LineaFactura;
import com.proyectoingsw.cafeteria.domain.Mesa;
import com.proyectoingsw.cafeteria.domain.Reporte;
import com.proyectoingsw.cafeteria.domain.Reserva;
import com.proyectoingsw.cafeteria.ui.CafeteriaApp;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class JdbcDatabaseTest {
    private final JdbcDatabase database = new JdbcDatabase(
            "jdbc:h2:mem:cafeteria_" + UUID.randomUUID().toString().replace("-", "")
                    + ";DB_CLOSE_DELAY=-1",
            "sa",
            "");

    @Test
    void foodCrudSurvivesDaoRecreationAndEnforcesStockConstraints() {
        JdbcAlimentoDAO alimentos = new JdbcAlimentoDAO(database);
        Alimento producto = alimento(41, 5);
        alimentos.agregar(producto);

        assertEquals(producto, new JdbcAlimentoDAO(database).buscarPorId(41));
        assertEquals(1, alimentos.buscarPorTipo("Bebida").size());
        assertTrue(alimentos.actualizar(alimento(41, 3)));
        assertEquals(3, new JdbcAlimentoDAO(database).buscarPorId(41).cantidadEnStock());
        assertThrows(
                PersistenceException.class,
                () -> alimentos.agregar(alimento(42, -1)));
        Alimento eliminado = alimentos.eliminar(41);
        assertEquals(producto.idAlimento(), eliminado.idAlimento());
        assertEquals(3, eliminado.cantidadEnStock());
        assertThrows(NoSuchElementException.class, () -> alimentos.buscarPorId(41));
    }

    @Test
    void persistsMesaTicketInvoiceReservationAndReportWithSearchIndexes() {
        JdbcMesaDAO mesas = new JdbcMesaDAO(database);
        Mesa mesa = new Mesa(7, true);
        mesas.agregar(mesa);
        assertEquals(mesa, mesas.buscarPorEstado(true).get(0));

        JdbcTicketDAO tickets = new JdbcTicketDAO(database);
        DefaultGestorTicket gestor = new DefaultGestorTicket(tickets, Clock.systemUTC());
        var ticket = gestor.generarTicket();
        assertTrue(new DefaultGestorTicket(new JdbcTicketDAO(database), Clock.systemUTC())
                .validarTicket(ticket));
        assertEquals(ticket, new JdbcTicketDAO(database).buscarPorId(ticket.idTicket()));

        LocalDate fecha = LocalDate.of(2026, 11, 12);
        JdbcFacturaDAO facturas = new JdbcFacturaDAO(database);
        Factura factura = new Factura(
                "F-7", fecha, 7, "Efectivo", new BigDecimal("10.50"), new BigDecimal("0.50"));
        facturas.agregar(factura);
        assertEquals(factura, facturas.buscarPorMetodoPago("Efectivo").get(0));
        assertEquals(factura, facturas.buscarPorFecha(fecha).get(0));
        JdbcFacturaTicketDAO facturaTicket = new JdbcFacturaTicketDAO(database);
        facturaTicket.asociar(factura.idFactura(), ticket.idTicket());
        assertEquals(ticket.idTicket(), facturaTicket.buscarTicket(factura.idFactura()));
        assertThrows(PersistenceException.class, () -> facturaTicket.asociar(
                factura.idFactura(), ticket.idTicket() + 1));

        JdbcReservaDAO reservas = new JdbcReservaDAO(database);
        Reserva reserva = new Reserva(8, fecha, "mediodia", "est-8", null);
        reservas.agregar(reserva);
        assertEquals(reserva, reservas.buscarPorEstudiante("est-8").get(0));
        assertEquals(reserva, reservas.buscarPorFranjaHoraria("mediodia").get(0));
        assertEquals(reserva, reservas.buscarPorFecha(fecha).get(0));
        assertTrue(reservas.buscarPorProfesor("prof-1").isEmpty());

        JdbcReporteDAO reportes = new JdbcReporteDAO(database);
        Reporte reporte = new Reporte("R-8", fecha, "mediodia", true, "resumen");
        reportes.agregar(reporte);
        assertEquals(reporte, reportes.buscarPorFecha(fecha).get(0));
        assertEquals(reporte, reportes.buscarPorFranjaHoraria("mediodia").get(0));
    }

    @Test
    void reservationMesaForeignKeyAndTransactionsProtectRelations() {
        JdbcMesaDAO mesas = new JdbcMesaDAO(database);
        mesas.agregar(new Mesa(1, false));
        JdbcReservaDAO reservas = new JdbcReservaDAO(database);
        reservas.agregar(new Reserva(
                1, LocalDate.of(2026, 11, 12), "mediodia", "est-1", null));

        database.withConnection(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(
                        "INSERT INTO RESERVA_MESA (ID_RESERVA, ID_MESA) VALUES (1, 1)");
            }
            return null;
        });
        assertThrows(PersistenceException.class, () -> database.withConnection(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(
                        "INSERT INTO RESERVA_MESA (ID_RESERVA, ID_MESA) VALUES (1, 99)");
            }
            return null;
        }));

        assertThrows(IllegalStateException.class, () -> database.inTransaction(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(
                        "UPDATE MESA SET RESERVADA = TRUE WHERE ID_MESA = 1");
            }
            throw new IllegalStateException("rollback");
        }));
        assertFalse(mesas.buscarPorId(1).reservada());
    }

    @Test
    void reservationMesaChangesAndLineItemsPersistAtomically() {
        JdbcMesaDAO mesas = new JdbcMesaDAO(database);
        mesas.agregar(new Mesa(2, false));
        JdbcReservaDAO reservas = new JdbcReservaDAO(database);
        reservas.agregar(new Reserva(
                2, LocalDate.of(2026, 11, 12), "tarde", "est-2", null));
        JdbcReservaMesaDAO relaciones = new JdbcReservaMesaDAO(database);

        relaciones.asociar(2, 2);
        assertTrue(mesas.buscarPorId(2).reservada());
        assertThrows(IllegalStateException.class, () -> relaciones.asociar(2, 2));
        relaciones.desasociar(2, 2);
        assertFalse(mesas.buscarPorId(2).reservada());

        JdbcLineaFacturaDAO lineas = new JdbcLineaFacturaDAO(database);
        LineaFactura linea = new LineaFactura(
                9, 2, new BigDecimal("3.25"), new BigDecimal("6.50"));
        lineas.agregar(linea);
        assertEquals(linea, new JdbcLineaFacturaDAO(database).buscarPorId(9));
    }

    @Test
    void upgradesLegacyTicketTimestampPrecision() throws Exception {
        String url = "jdbc:h2:mem:legacy_ticket_" + UUID.randomUUID().toString().replace("-", "")
                + ";DB_CLOSE_DELAY=-1";
        try (var connection = DriverManager.getConnection(url, "sa", "");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE TICKET ("
                    + "ID_TICKET INTEGER PRIMARY KEY, "
                    + "FECHA_GENERACION TIMESTAMP NOT NULL, "
                    + "VALIDO BOOLEAN NOT NULL, "
                    + "CODIGO_QR BLOB)");
        }

        JdbcDatabase migrated = new JdbcDatabase(url, "sa", "");
        JdbcTicketDAO tickets = new JdbcTicketDAO(migrated);
        Clock clock = Clock.fixed(
                Instant.parse("2026-11-13T23:59:59.123456789Z"), ZoneOffset.UTC);
        var ticket = new DefaultGestorTicket(tickets, clock).generarTicket();

        assertEquals(ticket, tickets.buscarPorId(ticket.idTicket()));
    }

    @Test
    void fileDatabasePersistsRowsAfterReinitialization() throws Exception {
        Path directory = Files.createTempDirectory("cafeteria-db-");
        String url = "jdbc:h2:file:" + directory.resolve("cafeteria")
                + ";DB_CLOSE_ON_EXIT=FALSE";
        try {
            JdbcDatabase firstOpen = new JdbcDatabase(url, "sa", "");
            int tableCount = firstOpen.withConnection(connection -> {
                try (var result = connection.createStatement().executeQuery(
                        "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES "
                                + "WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_TYPE = 'BASE TABLE'")) {
                    result.next();
                    return result.getInt(1);
                }
            });
            assertEquals(9, tableCount);

            SwingUtilities.invokeAndWait(() -> CafeteriaApp.crearPantallas(firstOpen));
            assertEquals(3, new JdbcAlimentoDAO(firstOpen).leerTodos().size());
            assertEquals(6, new JdbcMesaDAO(firstOpen).leerTodos().size());

            JdbcAlimentoDAO alimentos = new JdbcAlimentoDAO(firstOpen);
            alimentos.agregar(alimento(91, 4));
            assertEquals(alimento(91, 4), alimentos.buscarPorId(91));
            assertEquals(1, alimentos.buscarPorTipo("Bebida").stream()
                    .filter(item -> item.idAlimento() == 91).count());
            assertThrows(PersistenceException.class, () -> alimentos.agregar(alimento(92, -1)));

            JdbcMesaDAO mesas = new JdbcMesaDAO(firstOpen);
            mesas.agregar(new Mesa(91, false));
            JdbcReservaDAO reservas = new JdbcReservaDAO(firstOpen);
            reservas.agregar(new Reserva(
                    91, LocalDate.of(2026, 11, 12), "mediodia", "est-91", null));
            JdbcReservaMesaDAO relaciones = new JdbcReservaMesaDAO(firstOpen);
            relaciones.asociar(91, 91);
            assertTrue(mesas.buscarPorId(91).reservada());
            assertThrows(IllegalStateException.class, () -> relaciones.asociar(91, 91));

            JdbcDatabase restarted = new JdbcDatabase(url, "sa", "");
            assertEquals(alimento(91, 4), new JdbcAlimentoDAO(restarted).buscarPorId(91));
            assertTrue(new JdbcMesaDAO(restarted).buscarPorId(91).reservada());
            assertEquals(91, new JdbcReservaDAO(restarted).buscarPorEstudiante("est-91")
                    .get(0).idReserva());
            assertThrows(IllegalStateException.class, () -> restarted.inTransaction(connection -> {
                try (var statement = connection.createStatement()) {
                    statement.executeUpdate("UPDATE MESA SET RESERVADA = FALSE WHERE ID_MESA = 91");
                }
                throw new IllegalStateException("rollback");
            }));
            assertTrue(new JdbcMesaDAO(restarted).buscarPorId(91).reservada());

            SwingUtilities.invokeAndWait(() -> CafeteriaApp.crearPantallas(restarted));
            assertEquals(4, new JdbcAlimentoDAO(restarted).leerTodos().size());
            assertEquals(7, new JdbcMesaDAO(restarted).leerTodos().size());
        } finally {
            Files.deleteIfExists(directory.resolve("cafeteria.mv.db"));
            Files.deleteIfExists(directory.resolve("cafeteria.trace.db"));
            Files.deleteIfExists(directory.resolve("cafeteria.lock.db"));
            Files.delete(directory);
        }
    }

    private static Alimento alimento(int id, int stock) {
        return new Alimento(
                id,
                "Producto " + id,
                "Bebida",
                "Descripción",
                stock,
                new BigDecimal("2.50"));
    }
}
