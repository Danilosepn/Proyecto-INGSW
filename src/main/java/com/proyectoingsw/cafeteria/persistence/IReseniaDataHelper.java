package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Resenia;
import java.util.List;

public interface IReseniaDataHelper {
    List<Resenia> buscarPorTema(String tema);

    List<Resenia> buscarPorEstudiante(String codigoUnico);
}
