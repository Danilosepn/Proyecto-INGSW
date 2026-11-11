package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Reporte;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class InMemoryReporteDAO extends InMemoryDataHelperSQL<Reporte, String>
        implements ReporteDAO {
    public InMemoryReporteDAO() {
        super(Reporte::idReporte);
    }

    @Override
    public List<Reporte> buscarPorFecha(LocalDate fechaGeneracion) {
        Objects.requireNonNull(fechaGeneracion);
        return leerTodos().stream()
                .filter(reporte -> fechaGeneracion.equals(reporte.fechaGeneracion()))
                .toList();
    }

    @Override
    public List<Reporte> buscarPorFranjaHoraria(String franjaHoraria) {
        Objects.requireNonNull(franjaHoraria);
        return leerTodos().stream()
                .filter(reporte -> franjaHoraria.equals(reporte.franjaHoraria()))
                .toList();
    }
}
