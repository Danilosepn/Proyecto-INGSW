package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.Component;
import javax.swing.JOptionPane;

public final class VentanaError {
    private boolean visible;
    private String mensaje;

    public void mostrar(String mensaje) {
        mostrar(null, mensaje);
    }

    public void mostrar(Component padre, String mensaje) {
        this.mensaje = mensaje;
        visible = true;
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        visible = false;
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
