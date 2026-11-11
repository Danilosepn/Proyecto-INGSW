package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Resenia;
import java.util.List;

public interface GestorResenia {
    List<Resenia> obtenerResenias();

    void publicarResenia(Resenia resenia);
}
