package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.application.demo.DemoCafeteriaServices;
import com.proyectoingsw.cafeteria.domain.Alimento;
import com.proyectoingsw.cafeteria.domain.Mesa;
import com.proyectoingsw.cafeteria.domain.Usuario;
import com.proyectoingsw.cafeteria.persistence.AlimentoDAO;
import com.proyectoingsw.cafeteria.persistence.InMemoryAlimentoDAO;
import com.proyectoingsw.cafeteria.persistence.InMemoryMesaDAO;
import com.proyectoingsw.cafeteria.persistence.InMemoryTicketDAO;
import com.proyectoingsw.cafeteria.persistence.JdbcAlimentoDAO;
import com.proyectoingsw.cafeteria.persistence.JdbcDatabase;
import com.proyectoingsw.cafeteria.persistence.JdbcMesaDAO;
import com.proyectoingsw.cafeteria.persistence.JdbcTicketDAO;
import com.proyectoingsw.cafeteria.persistence.MesaDAO;
import com.proyectoingsw.cafeteria.persistence.TicketDAO;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import java.math.BigDecimal;

public final class CafeteriaApp {
    private CafeteriaApp() {
    }

    public static Map<String, PantallaBase> crearPantallas() {
        return crearPantallas(
                new InMemoryAlimentoDAO(),
                new InMemoryTicketDAO(),
                new InMemoryMesaDAO());
    }

    public static Map<String, PantallaBase> crearPantallas(JdbcDatabase database) {
        return crearPantallas(
                new JdbcAlimentoDAO(database),
                new JdbcTicketDAO(database),
                new JdbcMesaDAO(database));
    }

    private static Map<String, PantallaBase> crearPantallas(
            AlimentoDAO alimentos, TicketDAO tickets, MesaDAO mesas) {
        inicializarDatos(alimentos, mesas);
        DemoCafeteriaServices servicios = new DemoCafeteriaServices(alimentos, tickets, mesas);
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

    private static void inicializarDatos(AlimentoDAO alimentos, MesaDAO mesas) {
        if (alimentos.leerTodos().isEmpty()) {
            alimentos.agregar(new Alimento(
                    1, "Café de altura", "Bebida", "Café filtrado",
                    12, new BigDecimal("1.50")));
            alimentos.agregar(new Alimento(
                    2, "Sándwich vegetal", "Comida", "Sándwich de muestra",
                    6, new BigDecimal("3.25")));
            alimentos.agregar(new Alimento(
                    3, "Jugo de naranja", "Bebida", "Jugo de muestra",
                    0, new BigDecimal("2.00")));
        }
        if (mesas.leerTodos().isEmpty()) {
            for (int id = 1; id <= 6; id++) {
                mesas.agregar(new Mesa(id, false));
            }
        }
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("La interfaz Swing requiere un entorno gráfico.");
            return;
        }
        SwingUtilities.invokeLater(() -> new CafeteriaApp().iniciar());
    }

    private JFrame ventana;

    private void iniciar() {
        ventana = new JFrame("Cafetería");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane(new JLabel("Cargando la base de datos local..."));
        ventana.setMinimumSize(new Dimension(420, 180));
        ventana.setSize(520, 220);
        ventana.setLocationByPlatform(true);
        ventana.setVisible(true);

        new SwingWorker<JdbcDatabase, Void>() {
            @Override
            protected JdbcDatabase doInBackground() {
                return JdbcDatabase.fileBackedDefault();
            }

            @Override
            protected void done() {
                try {
                    mostrar(get());
                } catch (Exception error) {
                    String detalle = error.getCause() == null
                            ? error.getMessage() : error.getCause().getMessage();
                    javax.swing.JOptionPane.showMessageDialog(
                            ventana,
                            "No se pudo iniciar la base de datos local:\n" + detalle,
                            "Error de inicio",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    ventana.dispose();
                }
            }
        }.execute();
    }

    private void mostrar(JdbcDatabase database) {
        Map<String, PantallaBase> pantallas;
        try {
            pantallas = crearPantallas(database);
        } catch (RuntimeException error) {
            javax.swing.JOptionPane.showMessageDialog(
                    ventana,
                    "No se pudo iniciar la base de datos local:\n" + error.getMessage(),
                    "Error de inicio",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            ventana.dispose();
            return;
        }
        CardLayout tarjetas = new CardLayout();
        JPanel contenedor = new JPanel(tarjetas);
        pantallas.forEach((nombre, pantalla) -> contenedor.add(pantalla, nombre));

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
