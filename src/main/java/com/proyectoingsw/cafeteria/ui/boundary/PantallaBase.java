package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.BorderLayout;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public abstract class PantallaBase extends JPanel {
    private Consumer<String> navegador = ignored -> { };
    private final JLabel estado = new JLabel(
            "Modo demostración: las operaciones sin backend no se guardan.",
            SwingConstants.LEFT);
    private final JPanel contenido = new JPanel(new BorderLayout(12, 12));
    protected PantallaBase(String titulo) {
        super(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));

        JPanel encabezado = new JPanel(new BorderLayout(12, 0));
        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(etiquetaTitulo.getFont().deriveFont(22f));
        JButton inicio = new JButton("Menú principal");
        inicio.addActionListener(event -> navegar("menu"));
        encabezado.add(etiquetaTitulo, BorderLayout.WEST);
        encabezado.add(inicio, BorderLayout.EAST);

        estado.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        add(encabezado, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
        add(estado, BorderLayout.SOUTH);
    }

    public final void setNavegador(Consumer<String> navegador) {
        this.navegador = Objects.requireNonNull(navegador);
    }

    protected final JPanel contenido() {
        return contenido;
    }

    protected final void navegar(String destino) {
        navegador.accept(destino);
    }

    protected final void mostrarEstado(String mensaje) {
        estado.setText(Objects.requireNonNull(mensaje));
    }

}
