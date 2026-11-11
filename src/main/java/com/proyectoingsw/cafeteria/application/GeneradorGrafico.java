package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Reporte;

public interface GeneradorGrafico {
    void generarDiagramaBarrasPedidos();

    void generarDiagramaPastel();

    void generarResumenDePreguntas();

    Reporte generarReporte();
}
