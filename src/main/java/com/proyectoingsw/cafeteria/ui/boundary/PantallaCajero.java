package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.ControladorTickets;
import com.proyectoingsw.cafeteria.application.FacturaControlador;
import com.proyectoingsw.cafeteria.application.PagarEfectivo;
import java.util.Objects;

public final class PantallaCajero {
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
        this.vistaPedidos = new VistaPedidosCajero();
        this.controladorTickets = Objects.requireNonNull(controladorTickets);
        this.controlTickets = Objects.requireNonNull(controlTickets);
        this.pagos = Objects.requireNonNull(pagos);
        this.controlFacturas = Objects.requireNonNull(controlFacturas);
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
