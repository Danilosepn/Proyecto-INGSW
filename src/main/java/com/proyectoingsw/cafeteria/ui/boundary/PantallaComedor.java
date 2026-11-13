package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.SeleccionarAlimentoControlador;
import com.proyectoingsw.cafeteria.domain.Alimento;
import java.util.Locale;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PantallaComedor extends PantallaBase {
    private final VistaAlimentos menu;
    private final FormularioBusqueda busqueda;
    private final SeleccionarAlimentoControlador controlAlimentos;

    public PantallaComedor(SeleccionarAlimentoControlador controlAlimentos) {
        super("Comedor");
        this.menu = new VistaAlimentos(List.<Alimento>of());
        this.busqueda = new FormularioBusqueda();
        this.controlAlimentos = Objects.requireNonNull(controlAlimentos);
        busqueda.setAccionBusqueda(this::cargarMenu);
        JButton cargar = new JButton("Buscar alimentos");
        cargar.addActionListener(event -> cargarMenu());
        JButton pagar = new JButton("Ir a pago");
        pagar.addActionListener(event -> navegar("pago"));
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEADING));
        controles.add(cargar);
        controles.add(pagar);
        JPanel busquedaPanel = new JPanel(new BorderLayout(8, 8));
        busquedaPanel.add(busqueda, BorderLayout.CENTER);
        busquedaPanel.add(controles, BorderLayout.EAST);
        contenido().add(busquedaPanel, BorderLayout.NORTH);
        contenido().add(menu, BorderLayout.CENTER);
        cargarMenu();
    }

    private void cargarMenu() {
        try {
            List<Alimento> filtrados = solicitarBusqueda();
            menu.actualizarAlimentos(filtrados);
            mostrarEstado("%d alimentos encontrados en el catálogo local."
                    .formatted(filtrados.size()));
        } catch (RuntimeException error) {
            mostrarEstado(mensajeError(error));
        }
    }

    public List<Alimento> solicitarBusqueda() {
        String criterio = busqueda.obtenerCriterioBusqueda().toLowerCase(Locale.ROOT);
        return controlAlimentos.listarAlimentosDisponibles().stream()
                .filter(alimento -> alimento.nombre().toLowerCase(Locale.ROOT).contains(criterio)
                        || alimento.tipo().toLowerCase(Locale.ROOT).contains(criterio)
                        || alimento.descripcion().toLowerCase(Locale.ROOT).contains(criterio))
                .toList();
    }

    public void mostrar() {
        menu.mostrar();
        setVisible(true);
    }

    public SeleccionarAlimentoControlador getControlAlimentos() {
        return controlAlimentos;
    }

    public List<Alimento> getMenuAlimentos() {
        return menu.getMenuAlimentos();
    }

    public FormularioBusqueda getFormularioBusqueda() {
        return busqueda;
    }
}
