package com.proyectoingsw.cafeteria.domain;

import java.time.LocalDate;

public record Resenia(
        String idResenia,
        String nombreAutor,
        LocalDate fechaEnvio,
        String tema,
        boolean revisada) {
}
