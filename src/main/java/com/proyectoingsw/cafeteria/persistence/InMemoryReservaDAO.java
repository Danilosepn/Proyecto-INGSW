package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Reserva;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class InMemoryReservaDAO extends InMemoryDataHelperSQL<Reserva, Integer>
        implements ReservaDAO {
    public InMemoryReservaDAO() {
        super(Reserva::idReserva);
    }

    @Override
    public List<Reserva> buscarPorFecha(LocalDate fechaCreacion) {
        Objects.requireNonNull(fechaCreacion);
        return leerTodos().stream()
                .filter(reserva -> fechaCreacion.equals(reserva.fechaCreacion()))
                .toList();
    }

    @Override
    public List<Reserva> buscarPorEstudiante(String codigoUnico) {
        Objects.requireNonNull(codigoUnico);
        return leerTodos().stream()
                .filter(reserva -> codigoUnico.equals(reserva.codigoEstudiante()))
                .toList();
    }

    @Override
    public List<Reserva> buscarPorFranjaHoraria(String franjaHoraria) {
        Objects.requireNonNull(franjaHoraria);
        return leerTodos().stream()
                .filter(reserva -> franjaHoraria.equals(reserva.franjaHoraria()))
                .toList();
    }

    @Override
    public List<Reserva> buscarPorProfesor(String cedula) {
        Objects.requireNonNull(cedula);
        return leerTodos().stream()
                .filter(reserva -> cedula.equals(reserva.cedulaProfesor()))
                .toList();
    }
}
