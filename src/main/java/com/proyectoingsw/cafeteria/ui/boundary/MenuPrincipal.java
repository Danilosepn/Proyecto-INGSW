package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class MenuPrincipal extends PantallaBase {
    private final SelectorVentanas selectorVentanas;

    public MenuPrincipal(SelectorVentanas selectorVentanas) {
        super("Cafetería — menú principal");
        this.selectorVentanas = Objects.requireNonNull(selectorVentanas);
        selectorVentanas.setNavegador(this::navegar);
        JPanel opciones = new JPanel(new GridLayout(0, 2, 10, 10));
        agregarOpcion(opciones, "Comedor y alimentos", "comedor");
        agregarOpcion(opciones, "Mesas disponibles", "mesas");
        agregarOpcion(opciones, "Pago", "pago");
        agregarOpcion(opciones, "Ticket", "ticket");
        agregarOpcion(opciones, "Caja", "cajero");
        agregarOpcion(opciones, "Estadísticas", "estadisticas");
        agregarOpcion(opciones, "Iniciar sesión", "login");
        JButton salir = new JButton("Salir");
        salir.addActionListener(event -> navegar("salir"));
        opciones.add(salir);
        JPanel centro = new JPanel(new BorderLayout(8, 12));
        centro.add(new JLabel("Selecciona una pantalla. Los datos locales se conservan entre sesiones."),
                BorderLayout.NORTH);
        centro.add(opciones, BorderLayout.CENTER);
        centro.add(selectorVentanas, BorderLayout.SOUTH);
        contenido().add(centro, BorderLayout.CENTER);
    }

    private void agregarOpcion(JPanel panel, String texto, String destino) {
        JButton boton = new JButton(texto);
        boton.addActionListener(event -> {
            cambiarPantalla(destino);
        });
        panel.add(boton);
    }

    public SelectorVentanas getSelectorVentanas() {
        return selectorVentanas;
    }

    public void mostrar() {
        setVisible(true);
    }

    public void cambiarPantalla(String pantalla) {
        selectorVentanas.cambiarVentana(pantalla);
    }

}
