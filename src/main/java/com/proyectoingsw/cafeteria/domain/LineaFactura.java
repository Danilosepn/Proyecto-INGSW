package com.proyectoingsw.cafeteria.domain;

import java.math.BigDecimal;

public record LineaFactura(
        Integer numLineaCompra,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotalAlimento) {
}
