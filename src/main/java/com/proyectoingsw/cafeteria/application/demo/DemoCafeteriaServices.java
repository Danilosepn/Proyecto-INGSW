package com.proyectoingsw.cafeteria.application.demo;

import com.proyectoingsw.cafeteria.application.AutenticarUsuarioControlador;
import com.proyectoingsw.cafeteria.application.CalculadorEstadisticas;
import com.proyectoingsw.cafeteria.application.ComprarAlimentoManejador;
import com.proyectoingsw.cafeteria.application.ControladorTicket;
import com.proyectoingsw.cafeteria.application.ControladorTickets;
import com.proyectoingsw.cafeteria.application.DefaultGestorTicket;
import com.proyectoingsw.cafeteria.application.FacturaControlador;
import com.proyectoingsw.cafeteria.application.GeneradorGraficos;
import com.proyectoingsw.cafeteria.application.PagarEfectivo;
import com.proyectoingsw.cafeteria.application.ReservarMesaManejador;
import com.proyectoingsw.cafeteria.application.SeleccionarAlimentoControlador;
import com.proyectoingsw.cafeteria.domain.Alimento;
import com.proyectoingsw.cafeteria.domain.Compra;
import com.proyectoingsw.cafeteria.domain.Estudiante;
import com.proyectoingsw.cafeteria.domain.Factura;
import com.proyectoingsw.cafeteria.domain.Mesa;
import com.proyectoingsw.cafeteria.domain.Reporte;
import com.proyectoingsw.cafeteria.domain.Reserva;
import com.proyectoingsw.cafeteria.domain.Ticket;
import com.proyectoingsw.cafeteria.persistence.InMemoryAlimentoDAO;
import com.proyectoingsw.cafeteria.persistence.InMemoryMesaDAO;
import com.proyectoingsw.cafeteria.persistence.InMemoryTicketDAO;
import com.proyectoingsw.cafeteria.persistence.AlimentoDAO;
import com.proyectoingsw.cafeteria.persistence.MesaDAO;
import com.proyectoingsw.cafeteria.persistence.TicketDAO;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.Document;

/**
 * The UI adapter delegates food, mesa, and ticket persistence to injected DAOs.
 * Unsupported external integrations fail visibly when invoked.
 */
public final class DemoCafeteriaServices implements
        AutenticarUsuarioControlador,
        CalculadorEstadisticas,
        ComprarAlimentoManejador,
        ControladorTicket,
        ControladorTickets,
        FacturaControlador,
        GeneradorGraficos,
        PagarEfectivo,
        ReservarMesaManejador,
        SeleccionarAlimentoControlador {
    private final AlimentoDAO alimentos;
    private final MesaDAO mesas;
    private final DefaultGestorTicket gestorTicket;

    public DemoCafeteriaServices() {
        this(new InMemoryAlimentoDAO(), new InMemoryTicketDAO(), new InMemoryMesaDAO());
        alimentos.agregar(new Alimento(
                1, "Café de altura", "Bebida", "Café filtrado de demostración",
                12, new java.math.BigDecimal("1.50")));
        alimentos.agregar(new Alimento(
                2, "Sándwich vegetal", "Comida", "Sándwich de muestra",
                6, new java.math.BigDecimal("3.25")));
        alimentos.agregar(new Alimento(
                3, "Jugo de naranja", "Bebida", "Jugo de muestra",
                0, new java.math.BigDecimal("2.00")));
        for (int id = 1; id <= 6; id++) {
            mesas.agregar(new Mesa(id, false));
        }
    }

    public DemoCafeteriaServices(
            AlimentoDAO alimentos, TicketDAO tickets, MesaDAO mesas) {
        this.alimentos = Objects.requireNonNull(alimentos);
        this.mesas = Objects.requireNonNull(mesas);
        this.gestorTicket = new DefaultGestorTicket(
                Objects.requireNonNull(tickets), Clock.systemDefaultZone());
    }

    @Override
    public int consultarStockAlimento(Alimento alimento) {
        Objects.requireNonNull(alimento);
        return alimentos.buscarPorId(alimento.idAlimento()).cantidadEnStock();
    }

    @Override
    public List<Alimento> listarAlimentosPorTipo(String tipo) {
        return alimentos.buscarPorTipo(tipo);
    }

    @Override
    public List<Alimento> listarAlimentosDisponibles() {
        return alimentos.leerTodos().stream()
                .filter(alimento -> alimento.cantidadEnStock() > 0)
                .toList();
    }

    @Override
    public List<Alimento> listarAlimentosMenuDia(LocalDate fecha) {
        Objects.requireNonNull(fecha);
        return listarAlimentosDisponibles();
    }

    @Override
    public Ticket generarTicket() {
        return gestorTicket.generarTicket();
    }

    @Override
    public boolean validarTicket(Ticket ticket) {
        return gestorTicket.validarTicket(ticket);
    }

    @Override
    public boolean autenticar(String metodo, String usuario, String contrasena) {
        throw noConfigurado("Autenticación");
    }

    @Override
    public boolean autenticarEstudiante(String codigoUnico, String contrasena) {
        throw noConfigurado("Autenticación de estudiantes");
    }

    @Override
    public boolean autenticarPersonal(String usuario, String contrasena) {
        throw noConfigurado("Autenticación de personal");
    }

    @Override
    public boolean autenticarProfesor(String cedula, String contrasena) {
        throw noConfigurado("Autenticación de profesores");
    }

    @Override
    public Compra iniciarCompra() {
        throw noConfigurado("Compra y pago");
    }

    @Override
    public String verCarritoCompra(Compra compra) {
        throw noConfigurado("Carrito de compra");
    }

    @Override
    public void leerCuponDescuento(String codigo, double descuento) {
        throw noConfigurado("Cupones de descuento");
    }

    @Override
    public boolean pagarConEfectivo() {
        throw noConfigurado("Pago en efectivo");
    }

    @Override
    public int calcularVuelto() {
        throw noConfigurado("Cálculo de vuelto");
    }

    @Override
    public String generarRecibo() {
        throw noConfigurado("Generación de recibo");
    }

    @Override
    public boolean calcularTotalVentas(Ticket ticket) {
        throw noConfigurado("Estadísticas de ventas");
    }

    @Override
    public Reporte generarReporte() {
        throw noConfigurado("Generación de reportes");
    }

    @Override
    public int calcularTotalComidasVendidas() {
        throw noConfigurado("Estadísticas de comidas");
    }

    @Override
    public int calcularTotalReservasHechas() {
        throw noConfigurado("Estadísticas de reservas");
    }

    @Override
    public void generarDiagramaBarrasPedidos() {
        throw noConfigurado("Gráficos de pedidos");
    }

    @Override
    public void generarDiagramaPastel() {
        throw noConfigurado("Gráfico circular");
    }

    @Override
    public boolean crearReserva(Mesa mesa, Estudiante estudiante, String franjaHoraria) {
        throw noConfigurado("Reservas de mesas");
    }

    @Override
    public boolean modificarReserva(Reserva reserva, boolean cancelada) {
        throw noConfigurado("Modificación de reservas");
    }

    @Override
    public boolean consultarDisponibilidadMesa(Mesa mesa) {
        Objects.requireNonNull(mesa);
        return !mesas.buscarPorId(mesa.idMesa()).reservada();
    }

    @Override
    public List<Reserva> consultarHistorialReservas(Estudiante estudiante) {
        throw noConfigurado("Historial de reservas");
    }

    @Override
    public Factura generarFactura() {
        throw noConfigurado("Facturación");
    }

    @Override
    public String imprimirFacturaVirtual() {
        throw noConfigurado("Impresión de factura");
    }

    @Override
    public Document convertirAPDF(Factura factura) {
        throw noConfigurado("Exportación de factura a PDF");
    }

    private static UnsupportedOperationException noConfigurado(String operacion) {
        return new UnsupportedOperationException(
                operacion + " no está conectado en esta demostración.");
    }
}
