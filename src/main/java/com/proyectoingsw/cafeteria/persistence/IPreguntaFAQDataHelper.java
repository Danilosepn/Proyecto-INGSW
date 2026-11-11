package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.PreguntaFAQ;
import java.util.List;

public interface IPreguntaFAQDataHelper {
    List<PreguntaFAQ> buscarPorCategoria(String categoria);

    List<PreguntaFAQ> buscarPorFranjaHoraria(String franjaHoraria);
}
