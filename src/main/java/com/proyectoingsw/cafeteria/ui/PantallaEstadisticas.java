package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.application.CalculadorEstadisticas;
import com.proyectoingsw.cafeteria.application.GeneradorGraficos;
import java.util.Objects;

public final class PantallaEstadisticas {
    private final CalculadorEstadisticas calculador;
    private final GeneradorGraficos graficos;

    public PantallaEstadisticas(CalculadorEstadisticas calculador, GeneradorGraficos graficos) {
        this.calculador = Objects.requireNonNull(calculador);
        this.graficos = Objects.requireNonNull(graficos);
    }

    public CalculadorEstadisticas getCalculador() {
        return calculador;
    }

    public GeneradorGraficos getGraficos() {
        return graficos;
    }
}
