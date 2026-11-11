package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.PreguntaFAQ;
import com.proyectoingsw.cafeteria.persistence.PreguntarFAQDAO;
import java.util.List;
import java.util.Objects;

public final class DefaultAdministradorFAQ implements AdministradorFAQ {
    private final PreguntarFAQDAO preguntas;

    public DefaultAdministradorFAQ(PreguntarFAQDAO preguntas) {
        this.preguntas = Objects.requireNonNull(preguntas);
    }

    @Override
    public List<PreguntaFAQ> obtenerFAQs() {
        return preguntas.leerTodos();
    }

    @Override
    public void proponerFAQ(PreguntaFAQ pregunta) {
        preguntas.agregar(Objects.requireNonNull(pregunta));
    }
}
