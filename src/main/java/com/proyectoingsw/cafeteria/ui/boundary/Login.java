package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.AutenticarUsuarioControlador;
import com.proyectoingsw.cafeteria.domain.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class Login extends PantallaBase {
    private final FormularioLogin formulario;
    private final AutenticarUsuarioControlador autenticacion;
    private final Usuario usuario;
    private boolean visible;

    public Login(
            AutenticarUsuarioControlador autenticacion,
            Usuario usuario) {
        super("Iniciar sesión");
        this.formulario = new FormularioLogin();
        this.autenticacion = Objects.requireNonNull(autenticacion);
        this.usuario = Objects.requireNonNull(usuario);
        JButton ingresar = new JButton("Iniciar sesión");
        ingresar.addActionListener(event -> iniciarSesion());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEADING));
        acciones.add(ingresar);
        contenido().add(new JLabel(
                "La autenticación no está configurada en esta demostración."),
                BorderLayout.NORTH);
        contenido().add(formulario, BorderLayout.CENTER);
        contenido().add(acciones, BorderLayout.SOUTH);
    }

    private void iniciarSesion() {
        if (!formulario.validarCampos()) {
            mostrarEstado("Completa el tipo de usuario, usuario y contraseña.");
            return;
        }
        try {
            boolean autenticado = autenticacion.autenticar(
                    formulario.obtenerMetodoAutenticacion(),
                    formulario.obtenerParametros().get("usuario"),
                    formulario.obtenerParametros().get("contrasena"));
            if (autenticado) {
                mostrarEstado("Sesión iniciada.");
                navegar("menu");
            } else {
                mostrarEstado("Credenciales no aceptadas.");
            }
        } catch (UnsupportedOperationException error) {
            mostrarEstado(error.getMessage());
        }
    }

    public AutenticarUsuarioControlador getAutenticacion() {
        return autenticacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void mostrar() {
        visible = true;
        setVisible(true);
    }

    public void cerrar() {
        visible = false;
        setVisible(false);
    }

    public boolean isVisible() {
        return visible;
    }
}
