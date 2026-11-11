package com.proyectoingsw.cafeteria.ui;

public final class FormularioResenia {
    private boolean visible;
    private String mensaje;
    private String tema;

    public FormularioResenia(String tema) {
        this.tema = tema;
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}
