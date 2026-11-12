package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.AutenticarUsuarioControlador;
import com.proyectoingsw.cafeteria.domain.Usuario;
import java.util.Objects;

public final class Login {
    private final FormularioLogin formulario;
    private final AutenticarUsuarioControlador autenticacion;
    private final Usuario usuario;
    private boolean visible;

    public Login(
            AutenticarUsuarioControlador autenticacion,
            Usuario usuario) {
        this.formulario = new FormularioLogin();
        this.autenticacion = Objects.requireNonNull(autenticacion);
        this.usuario = Objects.requireNonNull(usuario);
    }

    public AutenticarUsuarioControlador getAutenticacion() {
        return autenticacion;
    }

    public Usuario getUsuario() {
        return usuario;
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
