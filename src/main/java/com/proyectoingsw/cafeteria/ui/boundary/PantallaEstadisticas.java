package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.CalculadorEstadisticas;
import com.proyectoingsw.cafeteria.application.GeneradorGraficos;
import com.proyectoingsw.cafeteria.domain.Mesa;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class PantallaEstadisticas extends PantallaBase {
    private final CalculadorEstadisticas calculador;
    private final GeneradorGraficos graficos;
    private final JComboBox<Mesa> selectorMesa;
    private final JLabel contextoMesa = new JLabel();

    public PantallaEstadisticas(CalculadorEstadisticas calculador, GeneradorGraficos graficos) {
        this(calculador, graficos, List.of());
    }

    public PantallaEstadisticas(
            CalculadorEstadisticas calculador,
            GeneradorGraficos graficos,
            List<Mesa> mesas) {
        super("Estadísticas");
        this.calculador = Objects.requireNonNull(calculador);
        this.graficos = Objects.requireNonNull(graficos);
        selectorMesa = new JComboBox<>(List.copyOf(mesas).toArray(Mesa[]::new));
        selectorMesa.setRenderer((list, mesa, index, isSelected, cellHasFocus) -> {
            String texto = mesa == null ? "Sin mesas disponibles"
                    : "Mesa %d — %s".formatted(
                            mesa.idMesa(), mesa.reservada() ? "reservada" : "disponible");
            JLabel item = new JLabel(texto);
            if (isSelected) {
                item.setOpaque(true);
                item.setBackground(list.getSelectionBackground());
                item.setForeground(list.getSelectionForeground());
            }
            return item;
        });
        selectorMesa.addActionListener(event -> actualizarContextoMesa());
        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEADING));
        selector.add(new JLabel("Mesa para el contexto de estadísticas:"));
        selector.add(selectorMesa);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton reporte = new JButton("Generar reporte");
        reporte.addActionListener(event -> ejecutarDemo(() -> calculador.generarReporte().toString()));
        JButton pedidos = new JButton("Gráfico de pedidos");
        pedidos.addActionListener(event -> ejecutarDemo(() -> {
            graficos.generarDiagramaBarrasPedidos();
            return "Gráfico de pedidos generado.";
        }));
        JButton reservas = new JButton("Gráfico de reservas");
        reservas.addActionListener(event -> ejecutarDemo(() -> {
            graficos.generarDiagramaPastel();
            return "Gráfico de reservas generado.";
        }));
        JButton verMesas = new JButton("Mesas disponibles");
        verMesas.addActionListener(event -> MesasDisponibles());
        acciones.add(reporte);
        acciones.add(pedidos);
        acciones.add(reservas);
        acciones.add(verMesas);
        contenido().add(new JLabel(
                "La Mesa seleccionada queda disponible en seleccionMesa(). "
                        + "Los contratos actuales de estadísticas generan resultados globales."),
                BorderLayout.NORTH);
        JPanel controles = new JPanel(new BorderLayout());
        controles.add(selector, BorderLayout.NORTH);
        controles.add(contextoMesa, BorderLayout.CENTER);
        controles.add(acciones, BorderLayout.SOUTH);
        contenido().add(controles, BorderLayout.CENTER);
        actualizarContextoMesa();
    }

    private void ejecutarDemo(java.util.function.Supplier<String> operacion) {
        try {
            mostrarEstado(operacion.get());
        } catch (RuntimeException error) {
            mostrarEstado(mensajeError(error));
        }
    }

    public void MesasDisponibles() {
        navegar("mesas");
    }

    public Mesa seleccionMesa() {
        return (Mesa) selectorMesa.getSelectedItem();
    }

    private void actualizarContextoMesa() {
        Mesa mesa = seleccionMesa();
        contextoMesa.setText(mesa == null
                ? "No hay mesas para seleccionar."
                : "Contexto seleccionado: Mesa %d (los servicios disponibles no filtran por Mesa)."
                        .formatted(mesa.idMesa()));
    }

    public CalculadorEstadisticas getCalculador() {
        return calculador;
    }

    public GeneradorGraficos getGraficos() {
        return graficos;
    }
}
