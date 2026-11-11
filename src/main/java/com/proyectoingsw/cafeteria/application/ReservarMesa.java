package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Estudiante;
import com.proyectoingsw.cafeteria.domain.Mesa;
import com.proyectoingsw.cafeteria.domain.Reserva;
import java.util.List;

public interface ReservarMesa {
    boolean crearReserva(Mesa mesa, Estudiante estudiante, String franjaHoraria);

    boolean modificarReserva(Reserva reserva, boolean cancelada);

    boolean consultarDisponibilidadMesa(Mesa mesa);

    List<Reserva> consultarHistorialReservas(Estudiante estudiante);
}
