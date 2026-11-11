package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.application.ReservarMesaManejador;
import com.proyectoingsw.cafeteria.domain.Mesa;
import java.util.List;
import java.util.Objects;

public final class PantallaMesasDisponibles {
    private List<Mesa> mesas;
    private final ReservarMesaManejador controlMesas;

    public PantallaMesasDisponibles(List<Mesa> mesas, ReservarMesaManejador controlMesas) {
        this.mesas = List.copyOf(mesas);
        this.controlMesas = Objects.requireNonNull(controlMesas);
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void actualizarMesas(List<Mesa> mesas) {
        this.mesas = List.copyOf(mesas);
    }

    public ReservarMesaManejador getControlMesas() {
        return controlMesas;
    }
}
