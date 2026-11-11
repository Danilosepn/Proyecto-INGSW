package com.proyectoingsw.cafeteria.domain;

import java.math.BigDecimal;

public record Alimento(
        Integer idAlimento,
        String nombre,
        String tipo,
        String descripcion,
        int cantidadEnStock,
        BigDecimal precioUnitario) {
}
