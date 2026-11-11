package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Reporte;
import java.time.LocalDate;
import java.util.List;

public interface IReporteDataHelper {
    List<Reporte> buscarPorFecha(LocalDate fechaGeneracion);

    List<Reporte> buscarPorFranjaHoraria(String franjaHoraria);
}
