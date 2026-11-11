package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Ticket;
import java.util.List;

public interface ITicketDataHelper {
    List<Ticket> buscarPorValido(boolean valido);
}
