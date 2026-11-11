package com.proyectoingsw.cafeteria.domain;

import java.time.LocalDate;

public record PreguntaFAQ(
        String numPregunta,
        LocalDate fechaRespuesta,
        String categoria,
        String pregunta,
        String respuesta) {
}
