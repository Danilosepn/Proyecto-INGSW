package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Alimento;
import java.time.LocalDate;
import java.util.List;

public interface SeleccionarAlimento {
    int consultarStockAlimento(Alimento alimento);

    List<Alimento> listarAlimentosPorTipo(String tipo);

    List<Alimento> listarAlimentosDisponibles();

    List<Alimento> listarAlimentosMenuDia(LocalDate fecha);
}
