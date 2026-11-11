package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.PreguntaFAQ;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class InMemoryPreguntaFAQDAO extends InMemoryDataHelperSQL<PreguntaFAQ, String>
        implements PreguntarFAQDAO {
    private final Function<PreguntaFAQ, String> franjaHorariaExtractor;

    public InMemoryPreguntaFAQDAO(Function<PreguntaFAQ, String> franjaHorariaExtractor) {
        super(PreguntaFAQ::numPregunta);
        this.franjaHorariaExtractor = Objects.requireNonNull(franjaHorariaExtractor);
    }

    @Override
    public List<PreguntaFAQ> buscarPorCategoria(String categoria) {
        Objects.requireNonNull(categoria);
        return leerTodos().stream()
                .filter(pregunta -> categoria.equals(pregunta.categoria()))
                .toList();
    }

    @Override
    public List<PreguntaFAQ> buscarPorFranjaHoraria(String franjaHoraria) {
        Objects.requireNonNull(franjaHoraria);
        return leerTodos().stream()
                .filter(pregunta -> franjaHoraria.equals(franjaHorariaExtractor.apply(pregunta)))
                .toList();
    }
}
