package com.proyectoingsw.cafeteria.application;

public interface AutenticarUsuario {
    boolean autenticar(String metodo, String usuario, String contrasena);

    boolean autenticarEstudiante(String codigoUnico, String contrasena);

    boolean autenticarPersonal(String usuario, String contrasena);

    boolean autenticarProfesor(String cedula, String contrasena);
}
