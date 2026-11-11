package com.proyectoingsw.cafeteria.ui;

public final class SelectorVentanas {
    private boolean visible;
    private String ventanaActual;

    public SelectorVentanas(String ventanaActual) {
        this.ventanaActual = ventanaActual;
    }

    public void cambiarVentana(String ventana) {
        ventanaActual = ventana;
    }

    public String generarVentana() {
        return ventanaActual;
    }

    public String getVentanaActual() {
        return ventanaActual;
    }

    public void mostrar() {
        visible = true;
    }

    public void ocultar() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }
}
