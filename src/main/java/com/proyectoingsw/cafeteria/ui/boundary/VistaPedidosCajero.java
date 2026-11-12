package com.proyectoingsw.cafeteria.ui.boundary;

public final class VistaPedidosCajero {
    private boolean visible;

    public void listarPedidos() {
        visible = true;
    }

    public void mostrarDatosTicket() {
        visible = true;
    }

    public void ocultar() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }
}
