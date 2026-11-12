package com.proyectoingsw.cafeteria.ui.boundary;

public final class VentanaError {
    private boolean visible;
    private String mensaje;

    public void mostrar(String mensaje) {
        this.mensaje = mensaje;
        visible = true;
    }

    public void cerrar() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getMensaje() {
        return mensaje;
    }
}
