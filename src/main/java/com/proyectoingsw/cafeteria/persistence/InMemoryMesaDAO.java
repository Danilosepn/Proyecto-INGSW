package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Mesa;
import java.util.List;

public final class InMemoryMesaDAO extends InMemoryDataHelperSQL<Mesa, Integer> implements MesaDAO {
    public InMemoryMesaDAO() {
        super(Mesa::idMesa);
    }

    @Override
    public List<Mesa> buscarPorEstado(boolean reservada) {
        return leerTodos().stream().filter(mesa -> mesa.reservada() == reservada).toList();
    }
}
