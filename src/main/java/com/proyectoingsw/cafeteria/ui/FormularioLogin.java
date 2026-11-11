package com.proyectoingsw.cafeteria.ui;

import java.util.Map;

public final class FormularioLogin {
    private boolean visible;
    private String metodoAutenticacion;
    private Map<String, String> parametros = Map.of();

    public boolean validarCampos() {
        return metodoAutenticacion != null && !parametros.isEmpty()
                && parametros.values().stream().allMatch(value -> value != null && !value.isBlank());
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
    }

    public void mostrar() {
        visible = true;
    }

    public void cerrar() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }
}
