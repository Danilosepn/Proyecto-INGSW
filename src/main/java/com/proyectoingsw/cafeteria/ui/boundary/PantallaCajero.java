package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.ControladorTickets;
import com.proyectoingsw.cafeteria.application.FacturaControlador;
import com.proyectoingsw.cafeteria.application.PagarEfectivo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PantallaCajero extends PantallaBase {
    private final VistaPedidosCajero vistaPedidos;
    private final ControladorTickets controladorTickets;
    private final ControladorTickets controlTickets;
    private final PagarEfectivo pagos;
    private final FacturaControlador controlFacturas;

    public PantallaCajero(
            ControladorTickets controladorTickets,
            ControladorTickets controlTickets,
            PagarEfectivo pagos,
            FacturaControlador controlFacturas) {
        super("Caja");
        this.vistaPedidos = new VistaPedidosCajero();
        this.controladorTickets = Objects.requireNonNull(controladorTickets);
        this.controlTickets = Objects.requireNonNull(controlTickets);
        this.pagos = Objects.requireNonNull(pagos);
        this.controlFacturas = Objects.requireNonNull(controlFacturas);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton detalle = new JButton("Ver pedido seleccionado");
        detalle.addActionListener(event -> {
            vistaPedidos.mostrarDatosTicket();
            String pedido = vistaPedidos.pedidoSeleccionado();
            mostrarEstado(pedido == null ? "Selecciona un pedido de demostración."
                    : pedido + " (datos de ejemplo; no es una orden real).");
        });
        JButton ticket = new JButton("Generar ticket");
        ticket.addActionListener(event -> ejecutarDemo(
                () -> {
                    var generado = controladorTickets.generarTicket();
                    boolean valido = controlTickets.validarTicket(generado);
                    return "Ticket #%d generado en memoria; validación: %s."
                            .formatted(generado.idTicket(), valido ? "válido" : "no válido");
                }));
        JButton pago = new JButton("Registrar pago en efectivo");
        pago.addActionListener(event -> ejecutarDemo(() -> Boolean.toString(pagos.pagarConEfectivo())));
        JButton factura = new JButton("Generar factura");
        factura.addActionListener(event -> ejecutarDemo(
                () -> controlFacturas.generarFactura().toString()));
        acciones.add(detalle);
        acciones.add(ticket);
        acciones.add(pago);
        acciones.add(factura);
        contenido().add(new JLabel(
                "Pedidos ilustrativos; tickets usan almacenamiento solo en memoria."),
                BorderLayout.NORTH);
        contenido().add(vistaPedidos, BorderLayout.CENTER);
        contenido().add(acciones, BorderLayout.SOUTH);
    }

    private void ejecutarDemo(java.util.function.Supplier<String> operacion) {
        try {
            mostrarEstado(operacion.get());
        } catch (UnsupportedOperationException error) {
            mostrarEstado(error.getMessage());
        }
    }

    public ControladorTickets getControladorTickets() {
        return controladorTickets;
    }

    public ControladorTickets getControlTickets() {
        return controlTickets;
    }

    public PagarEfectivo getPagos() {
        return pagos;
    }

    public FacturaControlador getControlFacturas() {
        return controlFacturas;
    }
}
