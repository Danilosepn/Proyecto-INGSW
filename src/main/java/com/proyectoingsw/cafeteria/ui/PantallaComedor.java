package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.application.SeleccionarAlimentoControlador;
import com.proyectoingsw.cafeteria.domain.Alimento;
import java.util.List;
import java.util.Objects;

public final class PantallaComedor {
    private final VistaAlimentos menu;
    private final FormularioBusqueda busqueda;
    private final SeleccionarAlimentoControlador controlAlimentos;

    public PantallaComedor(SeleccionarAlimentoControlador controlAlimentos) {
        this.menu = new VistaAlimentos(List.<Alimento>of());
        this.busqueda = new FormularioBusqueda();
        this.controlAlimentos = Objects.requireNonNull(controlAlimentos);
    }

    public SeleccionarAlimentoControlador getControlAlimentos() {
        return controlAlimentos;
    }
}
