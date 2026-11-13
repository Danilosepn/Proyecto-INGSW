package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class FormularioBusqueda extends JPanel {
    private String campoTexto = "";
    private final JTextField entrada = new JTextField(22);
    private Runnable buscar = () -> { };

    public FormularioBusqueda() {
        super(new BorderLayout(8, 0));
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 0));
        JButton limpiar = new JButton("Limpiar");
        limpiar.addActionListener(event -> limpiarCampo());
        controles.add(new JLabel("Buscar alimento:"));
        controles.add(entrada);
        controles.add(limpiar);
        add(controles, BorderLayout.CENTER);
        entrada.addActionListener(event -> buscar.run());
    }

    public void setAccionBusqueda(Runnable buscar) {
        this.buscar = Objects.requireNonNull(buscar);
    }

    public String obtenerCriterioBusqueda() {
        campoTexto = entrada.getText().trim();
        return campoTexto;
    }

    public void actualizarCriterio(String criterio) {
        campoTexto = criterio;
        entrada.setText(criterio);
    }

    public void limpiarCampo() {
        campoTexto = "";
        entrada.setText("");
    }
}
