package com.proyectoingsw.cafeteria.domain;

import java.time.LocalDate;

public record Reporte(
        String idReporte,
        LocalDate fechaGeneracion,
        String franjaHoraria,
        boolean graficos,
        String descripcion) {
}
