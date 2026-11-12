package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.application.ComprarAlimentoManejador;
import java.util.Objects;

public final class PantallaPago {
    private final FormularioPago formulario;
    private final ComprarAlimentoManejador control;

    public PantallaPago(ComprarAlimentoManejador control) {
        this.formulario = new FormularioPago("", "");
        this.control = Objects.requireNonNull(control);
    }

    public ComprarAlimentoManejador getControl() {
        return control;
    }
}
