package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Ticket;

public interface TicketDAO extends IDataHelperSQL<Ticket, Integer>, ITicketDataHelper {
}
