package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.ComprarAlimentoManejador;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PantallaPago extends PantallaBase {
    private final FormularioPago formulario;
    private final ComprarAlimentoManejador control;

    public PantallaPago(ComprarAlimentoManejador control) {
        super("Pago");
        this.formulario = new FormularioPago("Efectivo", "");
        this.control = Objects.requireNonNull(control);
        JButton procesar = new JButton("Procesar compra");
        procesar.addActionListener(event -> procesarCompra());
        JButton mostrarDatos = new JButton("Mostrar método seleccionado");
        mostrarDatos.addActionListener(event -> mostrarDatosPago());
        JButton ticket = new JButton("Abrir pantalla de ticket");
        ticket.addActionListener(event -> navegar("ticket"));
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEADING));
        acciones.add(procesar);
        acciones.add(mostrarDatos);
        acciones.add(ticket);
        contenido().add(new JLabel(
                "El pago está deshabilitado hasta conectar un proveedor; no se realiza ningún cargo."),
                BorderLayout.NORTH);
        contenido().add(formulario, BorderLayout.CENTER);
        contenido().add(acciones, BorderLayout.SOUTH);
    }

    private void procesarCompra() {
        String metodo = formulario.obtenerOpcionPago();
        try {
            mostrarEstado(control.verCarritoCompra(control.iniciarCompra()));
        } catch (RuntimeException error) {
            mostrarEstado("%s Método seleccionado: %s."
                    .formatted(mensajeError(error), metodo));
        }
    }

    public void mostrarDatosPago() {
        mostrarEstado("Método seleccionado: %s. Referencia solo de demostración; no se almacena."
                .formatted(formulario.obtenerOpcionPago()));
    }

    public ComprarAlimentoManejador getControl() {
        return control;
    }
}
