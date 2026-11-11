package com.proyectoingsw.cafeteria.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Factura(
        String idFactura,
        LocalDate fecha,
        Integer numeroPedido,
        String metodoPago,
        BigDecimal total,
        BigDecimal descuento) {
}
