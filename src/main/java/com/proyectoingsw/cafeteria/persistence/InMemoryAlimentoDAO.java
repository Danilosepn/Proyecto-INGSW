package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Alimento;
import java.util.List;
import java.util.Objects;

public final class InMemoryAlimentoDAO extends InMemoryDataHelperSQL<Alimento, Integer>
        implements AlimentoDAO {
    public InMemoryAlimentoDAO() {
        super(Alimento::idAlimento);
    }

    @Override
    public List<Alimento> buscarPorTipo(String categoria) {
        Objects.requireNonNull(categoria);
        return leerTodos().stream().filter(alimento -> categoria.equals(alimento.tipo())).toList();
    }
}
