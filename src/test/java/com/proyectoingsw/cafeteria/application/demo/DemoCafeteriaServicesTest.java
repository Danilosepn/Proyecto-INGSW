package com.proyectoingsw.cafeteria.application.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.proyectoingsw.cafeteria.domain.Ticket;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DemoCafeteriaServicesTest {
    @Test
    void suppliesAvailableDemoFoodAndAnInMemoryTicket() {
        DemoCafeteriaServices services = new DemoCafeteriaServices();

        assertEquals(2, services.listarAlimentosDisponibles().size());
        Ticket ticket = services.generarTicket();
        assertTrue(services.validarTicket(ticket));
        assertFalse(services.validarTicket(new Ticket(
                ticket.idTicket(), LocalDateTime.now(), true, ticket.codigoQR())));
    }

    @Test
    void reportsMissingExternalIntegrationsInsteadOfPretendingTheyWork() {
        DemoCafeteriaServices services = new DemoCafeteriaServices();

        UnsupportedOperationException error = assertThrows(
                UnsupportedOperationException.class,
                () -> services.autenticar("Personal", "demo", "demo"));
        assertTrue(error.getMessage().contains("no está conectado"));
    }
}
