package com.proyectoingsw.cafeteria.domain;

import java.time.LocalDate;

public record Reserva(
        Integer idReserva,
        LocalDate fechaCreacion,
        String franjaHoraria,
        String codigoEstudiante,
        String cedulaProfesor) {
}
