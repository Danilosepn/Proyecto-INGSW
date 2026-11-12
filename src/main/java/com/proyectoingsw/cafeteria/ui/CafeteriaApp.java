package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.application.demo.DemoCafeteriaServices;
import com.proyectoingsw.cafeteria.domain.Mesa;
import com.proyectoingsw.cafeteria.domain.Usuario;
import com.proyectoingsw.cafeteria.persistence.InMemoryMesaDAO;
import com.proyectoingsw.cafeteria.ui.boundary.Login;
import com.proyectoingsw.cafeteria.ui.boundary.MenuPrincipal;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaBase;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaCajero;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaComedor;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaEstadisticas;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaMesasDisponibles;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaPago;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaTicket;
import com.proyectoingsw.cafeteria.ui.boundary.SelectorVentanas;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class CafeteriaApp {
    private CafeteriaApp() {
    }

    public static Map<String, PantallaBase> crearPantallas() {
        DemoCafeteriaServices servicios = new DemoCafeteriaServices();
        InMemoryMesaDAO mesas = new InMemoryMesaDAO();
        for (int id = 1; id <= 6; id++) {
            mesas.agregar(new Mesa(id, id == 3));
        }
        var mesasDisponibles = mesas.leerTodos();

        Map<String, PantallaBase> pantallas = new LinkedHashMap<>();
        pantallas.put("menu", new MenuPrincipal(new SelectorVentanas("comedor")));
        pantallas.put("comedor", new PantallaComedor(servicios));
        pantallas.put("mesas", new PantallaMesasDisponibles(mesasDisponibles, servicios));
        pantallas.put("pago", new PantallaPago(servicios));
        pantallas.put("ticket", new PantallaTicket(servicios));
        pantallas.put("cajero", new PantallaCajero(servicios, servicios, servicios, servicios));
        pantallas.put("estadisticas", new PantallaEstadisticas(
                servicios, servicios, mesasDisponibles));
        pantallas.put("login", new Login(servicios, new Usuario("Demostración", "no-autenticado")));
        return Collections.unmodifiableMap(pantallas);
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("La interfaz Swing requiere un entorno gráfico.");
            return;
        }
        SwingUtilities.invokeLater(() -> new CafeteriaApp().mostrar());
    }

    private void mostrar() {
        Map<String, PantallaBase> pantallas = crearPantallas();
        CardLayout tarjetas = new CardLayout();
        JPanel contenedor = new JPanel(tarjetas);
        pantallas.forEach(contenedor::add);

        JFrame ventana = new JFrame("Cafetería");
        pantallas.values().forEach(pantalla -> pantalla.setNavegador(destino -> {
            if ("salir".equals(destino)) {
                ventana.dispose();
            } else if (pantallas.containsKey(destino)) {
                tarjetas.show(contenedor, destino);
            } else {
                throw new IllegalArgumentException("Pantalla desconocida: " + destino);
            }
        }));
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane(contenedor);
        ventana.setMinimumSize(new Dimension(760, 520));
        ventana.setSize(980, 700);
        ventana.setLocationByPlatform(true);
        tarjetas.show(contenedor, "menu");
        ventana.setVisible(true);
    }
}
