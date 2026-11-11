package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Resenia;
import com.proyectoingsw.cafeteria.persistence.ReseniaDAO;
import java.util.List;
import java.util.Objects;

public final class DefaultGestorResenia implements GestorResenia {
    private final ReseniaDAO resenias;

    public DefaultGestorResenia(ReseniaDAO resenias) {
        this.resenias = Objects.requireNonNull(resenias);
    }

    @Override
    public List<Resenia> obtenerResenias() {
        return resenias.leerTodos();
    }

    @Override
    public void publicarResenia(Resenia resenia) {
        resenias.agregar(Objects.requireNonNull(resenia));
    }
}
