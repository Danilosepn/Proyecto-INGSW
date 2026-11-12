package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class FormularioPago extends JPanel {
    private String opcionPago;
    private String datosPago;
    private final JComboBox<String> opcion = new JComboBox<>(
            new String[] {"Efectivo", "Tarjeta", "Transferencia"});
    private final JTextField datos = new JTextField(20);

    public FormularioPago(String opcionPago, String datosPago) {
        super(new GridBagLayout());
        this.opcionPago = opcionPago;
        this.datosPago = datosPago;
        opcion.setSelectedItem(opcionPago);
        datos.setText(datosPago);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Método de pago:"), constraints);
        constraints.gridx = 1;
        add(opcion, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(new JLabel("Referencia/datos (demostración):"), constraints);
        constraints.gridx = 1;
        add(datos, constraints);
    }

    public String obtenerOpcionPago() {
        opcionPago = (String) opcion.getSelectedItem();
        return opcionPago;
    }

    public String obtenerDatosPago() {
        datosPago = datos.getText().trim();
        return datosPago;
    }

    public void actualizar(String opcionPago, String datosPago) {
        this.opcionPago = opcionPago;
        this.datosPago = datosPago;
        opcion.setSelectedItem(opcionPago);
        datos.setText(datosPago);
    }
}
