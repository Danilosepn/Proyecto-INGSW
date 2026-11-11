package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Reserva;
import java.time.LocalDate;
import java.util.List;

public interface IReservaDataHelper {
    List<Reserva> buscarPorFecha(LocalDate fechaCreacion);

    List<Reserva> buscarPorEstudiante(String codigoUnico);

    List<Reserva> buscarPorFranjaHoraria(String franjaHoraria);

    List<Reserva> buscarPorProfesor(String cedula);
}
