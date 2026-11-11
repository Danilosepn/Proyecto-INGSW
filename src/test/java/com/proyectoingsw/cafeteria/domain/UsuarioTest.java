package com.proyectoingsw.cafeteria.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class UsuarioTest {
    @Test
    void doesNotExposePasswordInGeneratedText() {
        Usuario usuario = new Usuario("ana", "secreto");

        assertFalse(usuario.toString().contains("secreto"));
    }
}
