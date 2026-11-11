package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Resenia;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class InMemoryReseniaDAO extends InMemoryDataHelperSQL<Resenia, String>
        implements ReseniaDAO {
    private final Function<Resenia, String> codigoEstudianteExtractor;

    public InMemoryReseniaDAO(Function<Resenia, String> codigoEstudianteExtractor) {
        super(Resenia::idResenia);
        this.codigoEstudianteExtractor = Objects.requireNonNull(codigoEstudianteExtractor);
    }

    @Override
    public List<Resenia> buscarPorTema(String tema) {
        Objects.requireNonNull(tema);
        return leerTodos().stream().filter(resenia -> tema.equals(resenia.tema())).toList();
    }

    @Override
    public List<Resenia> buscarPorEstudiante(String codigoUnico) {
        Objects.requireNonNull(codigoUnico);
        return leerTodos().stream()
                .filter(resenia -> codigoUnico.equals(codigoEstudianteExtractor.apply(resenia)))
                .toList();
    }
}
