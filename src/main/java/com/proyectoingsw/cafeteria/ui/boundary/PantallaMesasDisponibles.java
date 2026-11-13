package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.ReservarMesaManejador;
import com.proyectoingsw.cafeteria.domain.Mesa;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class PantallaMesasDisponibles extends PantallaBase {
    private List<Mesa> mesas;
    private final ReservarMesaManejador controlMesas;
    private final DefaultListModel<Mesa> modelo = new DefaultListModel<>();
    private final JList<Mesa> lista = new JList<>(modelo);

    public PantallaMesasDisponibles(List<Mesa> mesas, ReservarMesaManejador controlMesas) {
        super("Mesas disponibles");
        this.mesas = List.copyOf(mesas);
        this.controlMesas = Objects.requireNonNull(controlMesas);
        lista.setCellRenderer((list, mesa, index, isSelected, cellHasFocus) -> {
            JLabel item = new JLabel("Mesa %d — %s".formatted(
                    mesa.idMesa(), mesa.reservada() ? "reservada (muestra)" : "libre (muestra)"));
            if (isSelected) {
                item.setOpaque(true);
                item.setBackground(list.getSelectionBackground());
                item.setForeground(list.getSelectionForeground());
            }
            return item;
        });
        JButton consultar = new JButton("Consultar disponibilidad");
        consultar.addActionListener(event -> consultarDisponibilidad());
        JButton actualizar = new JButton("Mostrar mesas de demostración");
        actualizar.addActionListener(event -> MesasDisponibles());
        JButton volver = new JButton("Volver al menú");
        volver.addActionListener(event -> navegar("menu"));
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEADING));
        acciones.add(consultar);
        acciones.add(actualizar);
        acciones.add(volver);
        contenido().add(new JLabel(
                "Estado de mesas cargado desde el almacenamiento local."),
                BorderLayout.NORTH);
        contenido().add(new JScrollPane(lista), BorderLayout.CENTER);
        contenido().add(acciones, BorderLayout.SOUTH);
        actualizarMesas(this.mesas);
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void actualizarMesas(List<Mesa> mesas) {
        this.mesas = List.copyOf(mesas);
        modelo.clear();
        this.mesas.forEach(modelo::addElement);
    }

    public ReservarMesaManejador getControlMesas() {
        return controlMesas;
    }

    public Mesa seleccionMesa() {
        return lista.getSelectedValue();
    }

    public void MesasDisponibles() {
        actualizarMesas(mesas);
        setVisible(true);
        mostrarEstado("%d mesas de demostración mostradas.".formatted(mesas.size()));
    }

    private void consultarDisponibilidad() {
        Mesa seleccionada = seleccionMesa();
        if (seleccionada == null) {
            mostrarEstado("Selecciona una mesa de demostración.");
            return;
        }
        try {
            mostrarEstado(controlMesas.consultarDisponibilidadMesa(seleccionada)
                    ? "Mesa disponible." : "Mesa no disponible.");
        } catch (RuntimeException error) {
            mostrarEstado(mensajeError(error));
        }
    }
}
