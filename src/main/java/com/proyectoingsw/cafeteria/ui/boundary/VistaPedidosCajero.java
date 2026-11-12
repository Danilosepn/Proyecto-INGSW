package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class VistaPedidosCajero extends JPanel {
    private boolean visible;
    private final DefaultListModel<String> pedidos = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(pedidos);
    private final JLabel detalleTicket = new JLabel("Selecciona un pedido para ver sus datos.");

    public VistaPedidosCajero() {
        super(new BorderLayout(8, 8));
        add(new JLabel("Pedidos de ejemplo (datos no persistidos)"), BorderLayout.NORTH);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(detalleTicket, BorderLayout.SOUTH);
        listarPedidos();
    }

    public void listarPedidos() {
        visible = true;
        setVisible(true);
        pedidos.clear();
        pedidos.addElement("Pedido demo #101 — 2 cafés");
        pedidos.addElement("Pedido demo #102 — 1 sándwich");
        pedidos.addElement("Pedido demo #103 — 1 jugo");
    }

    public void mostrarDatosTicket() {
        visible = true;
        setVisible(true);
        String pedido = lista.getSelectedValue();
        detalleTicket.setText(pedido == null
                ? "Selecciona un pedido para ver sus datos."
                : pedido + " (datos de ejemplo; no es un ticket real).");
    }

    public void ocultar() {
        visible = false;
        setVisible(false);
    }

    public boolean isVisible() {
        return visible;
    }

    public String pedidoSeleccionado() {
        return lista.getSelectedValue();
    }
}
