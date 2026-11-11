package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Alimento;
import java.util.List;

public interface IAlimentoDataHelper {
    List<Alimento> buscarPorTipo(String categoria);
}
