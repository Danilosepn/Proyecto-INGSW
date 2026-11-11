package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.PreguntaFAQ;
import java.util.List;

public interface AdministradorFAQ {
    List<PreguntaFAQ> obtenerFAQs();

    void proponerFAQ(PreguntaFAQ pregunta);
}
