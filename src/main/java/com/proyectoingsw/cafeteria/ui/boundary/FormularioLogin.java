package com.proyectoingsw.cafeteria.ui.boundary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public final class FormularioLogin extends JPanel {
    private String metodoAutenticacion;
    private Map<String, String> parametros = Map.of();
    private final JComboBox<String> metodo = new JComboBox<>(
            new String[] {"Personal", "Estudiante", "Profesor"});
    private final JTextField usuario = new JTextField(18);
    private final JPasswordField contrasena = new JPasswordField(18);

    public FormularioLogin() {
        super(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        addField(constraints, 0, "Tipo de usuario:", metodo);
        addField(constraints, 1, "Usuario o código:", usuario);
        addField(constraints, 2, "Contraseña:", contrasena);
    }

    private void addField(GridBagConstraints constraints, int row, String label, java.awt.Component field) {
        constraints.gridx = 0;
        constraints.gridy = row;
        add(new JLabel(label), constraints);
        constraints.gridx = 1;
        add(field, constraints);
    }

    public boolean validarCampos() {
        metodoAutenticacion = (String) metodo.getSelectedItem();
        parametros = Map.of(
                "usuario", usuario.getText().trim(),
                "contrasena", new String(contrasena.getPassword()));
        return metodoAutenticacion != null
                && parametros.values().stream().allMatch(value -> !value.isBlank());
    }

    public String obtenerMetodoAutenticacion() {
        return metodoAutenticacion;
    }

    public Map<String, String> obtenerParametros() {
        return parametros;
    }

    public void configurar(String metodoAutenticacion, Map<String, String> parametros) {
        this.metodoAutenticacion = metodoAutenticacion;
        this.parametros = Map.copyOf(parametros);
        metodo.setSelectedItem(metodoAutenticacion);
        usuario.setText(parametros.getOrDefault("usuario", ""));
        contrasena.setText(parametros.getOrDefault("contrasena", ""));
    }

    public void mostrar() {
        setVisible(true);
    }

    public void cerrar() {
        setVisible(false);
    }
}
