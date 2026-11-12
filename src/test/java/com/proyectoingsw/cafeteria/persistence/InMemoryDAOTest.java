package com.proyectoingsw.cafeteria.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.proyectoingsw.cafeteria.domain.Alimento;
import com.proyectoingsw.cafeteria.domain.Reserva;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class InMemoryDAOTest {
    @Test
    void genericCrudAndFoodSearchUseTypedIdentifiers() {
        InMemoryAlimentoDAO dao = new InMemoryAlimentoDAO();
        Alimento first = alimento(1, "Bebida");
        dao.agregar(first);

        assertEquals(first, dao.buscarPorId(1));
        assertEquals(List.of(first), dao.buscarPorTipo("Bebida"));
        assertFalse(dao.actualizar(alimento(2, "Comida")));
        Alimento updated = alimento(1, "Comida");
        assertTrue(dao.actualizar(updated));
        assertEquals("Comida", dao.buscarPorId(1).tipo());
        assertThrows(IllegalStateException.class, () -> dao.agregar(first));
        assertThrows(UnsupportedOperationException.class, () -> dao.leerTodos().clear());
        assertEquals(updated, dao.eliminar(1));
        assertThrows(NoSuchElementException.class, () -> dao.buscarPorId(1));
    }

    @Test
    void reservationSearchesMatchTheDiagramFields() {
        InMemoryReservaDAO dao = new InMemoryReservaDAO();
        Reserva reservation = new Reserva(
                7, LocalDate.of(2026, 11, 11), "mediodia", "est-1", null);
        dao.agregar(reservation);

        assertEquals(List.of(reservation), dao.buscarPorEstudiante("est-1"));
        assertEquals(List.of(reservation), dao.buscarPorFranjaHoraria("mediodia"));
        assertEquals(List.of(reservation), dao.buscarPorFecha(LocalDate.of(2026, 11, 11)));
        assertTrue(dao.buscarPorProfesor("prof-1").isEmpty());
    }

    private static Alimento alimento(int id, String tipo) {
        return new Alimento(
                id,
                "Producto " + id,
                tipo,
                "",
                10,
                new BigDecimal("2.50"));
    }
}
