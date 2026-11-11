package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Ticket;
import java.util.List;

public final class InMemoryTicketDAO extends InMemoryDataHelperSQL<Ticket, Integer>
        implements TicketDAO {
    public InMemoryTicketDAO() {
        super(Ticket::idTicket);
    }

    @Override
    public List<Ticket> buscarPorValido(boolean valido) {
        return leerTodos().stream().filter(ticket -> ticket.valido() == valido).toList();
    }
}
