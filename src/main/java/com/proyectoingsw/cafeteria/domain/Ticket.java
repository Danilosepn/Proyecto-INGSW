package com.proyectoingsw.cafeteria.domain;

import com.google.zxing.common.BitMatrix;
import java.time.LocalDateTime;

public record Ticket(
        Integer idTicket,
        LocalDateTime fechaGeneracion,
        boolean valido,
        BitMatrix codigoQR) {
}
