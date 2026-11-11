package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.application.ControladorTicket;
import java.util.Objects;

public final class PantallaTicket {
    private final ControladorTicket controlTicket;
    private boolean visible;

    public PantallaTicket(ControladorTicket controlTicket) {
        this.controlTicket = Objects.requireNonNull(controlTicket);
    }

    public ControladorTicket getControlTicket() {
        return controlTicket;
    }

    public void mostrarTicket() {
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }
}
