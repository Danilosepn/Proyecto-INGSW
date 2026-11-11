package com.proyectoingsw.cafeteria.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.zxing.common.BitMatrix;
import com.proyectoingsw.cafeteria.domain.Ticket;
import com.proyectoingsw.cafeteria.persistence.InMemoryTicketDAO;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class DefaultGestorTicketTest {
    @Test
    void createsPersistedQrTicketsAndValidatesTheirState() {
        InMemoryTicketDAO dao = new InMemoryTicketDAO();
        Clock clock = Clock.fixed(Instant.parse("2026-11-11T17:00:00Z"), ZoneOffset.UTC);
        DefaultGestorTicket manager = new DefaultGestorTicket(dao, clock);

        Ticket ticket = manager.generarTicket();

        assertEquals(1, ticket.idTicket());
        assertEquals(Instant.parse("2026-11-11T17:00:00Z"), ticket.fechaGeneracion()
                .toInstant(ZoneOffset.UTC));
        assertNotNull(ticket.codigoQR());
        assertEquals(ticket, dao.buscarPorId(ticket.idTicket()));
        assertTrue(manager.validarTicket(ticket));
        assertFalse(manager.validarTicket(new Ticket(
                ticket.idTicket(), ticket.fechaGeneracion(), false, ticket.codigoQR())));
        assertFalse(manager.validarTicket(new Ticket(
                ticket.idTicket(), ticket.fechaGeneracion(), true, new BitMatrix(1, 1))));
        assertFalse(manager.validarTicket(new Ticket(99, ticket.fechaGeneracion(), true, null)));
    }
}
