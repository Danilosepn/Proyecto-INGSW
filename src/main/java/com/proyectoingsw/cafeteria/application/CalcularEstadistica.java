package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Reporte;
import com.proyectoingsw.cafeteria.domain.Ticket;

public interface CalcularEstadistica {
    boolean calcularTotalVentas(Ticket ticket);

    Reporte generarReporte();

    int calcularTotalComidasVendidas();

    int calcularTotalReservasHechas();
}
