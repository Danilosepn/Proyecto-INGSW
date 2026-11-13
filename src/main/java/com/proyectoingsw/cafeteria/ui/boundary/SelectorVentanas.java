package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.FlowLayout;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class SelectorVentanas extends JPanel {
    private String ventanaActual;
    private Consumer<String> navegador = ignored -> { };
    private final JComboBox<String> destinos = new JComboBox<>(new String[] {
        "comedor", "mesas", "pago", "ticket", "cajero", "estadisticas", "login"
    });

    public SelectorVentanas(String ventanaActual) {
        super(new FlowLayout(FlowLayout.LEADING, 8, 0));
        this.ventanaActual = ventanaActual;
        destinos.setSelectedItem(ventanaActual);
        add(new JLabel("Ir a:"));
        add(destinos);
        JButton cambiar = new JButton("Abrir");
        cambiar.addActionListener(event -> cambiarVentana((String) destinos.getSelectedItem()));
        add(cambiar);
    }

    public void cambiarVentana(String ventana) {
        ventanaActual = ventana;
        destinos.setSelectedItem(ventana);
        navegador.accept(ventana);
    }

    public void setNavegador(Consumer<String> navegador) {
        this.navegador = Objects.requireNonNull(navegador);
    }

    public String generarVentana() {
        return ventanaActual;
    }

    public String getVentanaActual() {
        return ventanaActual;
    }

    public void mostrar() {
        setVisible(true);
    }

    public void ocultar() {
        setVisible(false);
    }
}
