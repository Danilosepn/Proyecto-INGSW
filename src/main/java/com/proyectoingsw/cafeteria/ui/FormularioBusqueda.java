package com.proyectoingsw.cafeteria.ui;

public final class FormularioBusqueda {
    private String campoTexto = "";

    public String obtenerCriterioBusqueda() {
        return campoTexto;
    }

    public void actualizarCriterio(String criterio) {
        campoTexto = criterio;
    }

    public void limpiarCampo() {
        campoTexto = "";
    }
}
