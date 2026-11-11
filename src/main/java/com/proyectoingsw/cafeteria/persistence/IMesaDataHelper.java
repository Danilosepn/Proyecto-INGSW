package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Mesa;
import java.util.List;

public interface IMesaDataHelper {
    List<Mesa> buscarPorEstado(boolean reservada);
}
