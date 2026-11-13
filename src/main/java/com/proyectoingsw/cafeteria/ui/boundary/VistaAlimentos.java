package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.BorderLayout;
import com.proyectoingsw.cafeteria.domain.Alimento;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class VistaAlimentos extends JPanel {
    private List<Alimento> menuAlimentos;
    private final DefaultListModel<Alimento> modelo = new DefaultListModel<>();
    private final JList<Alimento> lista = new JList<>(modelo);
    private final JLabel seleccionado = new JLabel("Selecciona un alimento.");

    public VistaAlimentos(List<Alimento> menuAlimentos) {
        super(new BorderLayout(8, 8));
        this.menuAlimentos = List.copyOf(menuAlimentos);
        lista.setCellRenderer((list, alimento, index, isSelected, cellHasFocus) -> {
            String texto = "%s — %s — $%s — stock %d".formatted(
                    alimento.nombre(), alimento.tipo(), alimento.precioUnitario(),
                    alimento.cantidadEnStock());
            JLabel item = new JLabel(texto);
            if (isSelected) {
                item.setOpaque(true);
                item.setBackground(list.getSelectionBackground());
                item.setForeground(list.getSelectionForeground());
            }
            return item;
        });
        lista.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                Alimento alimento = lista.getSelectedValue();
                seleccionado.setText(alimento == null
                        ? "Selecciona un alimento."
                        : "%s — %s".formatted(alimento.nombre(), alimento.descripcion()));
            }
        });
        add(new JLabel("Catálogo local de alimentos"), BorderLayout.NORTH);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(seleccionado, BorderLayout.SOUTH);
        actualizarAlimentos(menuAlimentos);
    }

    public List<Alimento> getMenuAlimentos() {
        return menuAlimentos;
    }

    public void actualizarAlimentos(List<Alimento> alimentos) {
        menuAlimentos = List.copyOf(alimentos);
        modelo.clear();
        menuAlimentos.forEach(modelo::addElement);
    }

    public void ocultar() {
        setVisible(false);
    }

    public void mostrar() {
        setVisible(true);
    }

    public Alimento obtenerSeleccion() {
        return lista.getSelectedValue();
    }

    public Alimento seleccionarAlimento() {
        return lista.getSelectedValue();
    }
}
