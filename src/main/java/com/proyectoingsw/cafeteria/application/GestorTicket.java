package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Ticket;

public interface GestorTicket {
    Ticket generarTicket();

    boolean validarTicket(Ticket ticket);
}
