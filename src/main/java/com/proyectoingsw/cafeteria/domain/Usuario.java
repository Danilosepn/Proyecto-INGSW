package com.proyectoingsw.cafeteria.domain;

public record Usuario(String nombre, String contrasena) {
    @Override
    public String toString() {
        return "Usuario[nombre=" + nombre + ", contrasena=[REDACTED]]";
    }
}
