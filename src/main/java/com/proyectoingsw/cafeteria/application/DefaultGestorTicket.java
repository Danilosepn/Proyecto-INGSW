package com.proyectoingsw.cafeteria.application;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.proyectoingsw.cafeteria.domain.Ticket;
import com.proyectoingsw.cafeteria.persistence.TicketDAO;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class DefaultGestorTicket implements GestorTicket {
    private static final int QR_SIZE = 200;
    private final TicketDAO tickets;
    private final Clock clock;

    public DefaultGestorTicket(TicketDAO tickets, Clock clock) {
        this.tickets = Objects.requireNonNull(tickets);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public synchronized Ticket generarTicket() {
        int nextId = tickets.leerTodos().stream()
                .map(Ticket::idTicket)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .map(id -> Math.addExact(id, 1))
                .orElse(1);
        String payload = "ticket:" + nextId;
        try {
            Ticket ticket = new Ticket(
                    nextId,
                    LocalDateTime.now(clock),
                    true,
                    new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE));
            tickets.agregar(ticket);
            return ticket;
        } catch (WriterException exception) {
            throw new IllegalStateException("Could not generate the ticket QR code.", exception);
        }
    }

    @Override
    public boolean validarTicket(Ticket ticket) {
        Objects.requireNonNull(ticket);
        if (ticket.idTicket() == null || !ticket.valido()) {
            return false;
        }
        try {
            Ticket stored = tickets.buscarPorId(ticket.idTicket());
            return stored.valido()
                    && stored.fechaGeneracion().equals(ticket.fechaGeneracion())
                    && sameMatrix(stored.codigoQR(), ticket.codigoQR());
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    private static boolean sameMatrix(BitMatrix first, BitMatrix second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null
                || first.getWidth() != second.getWidth()
                || first.getHeight() != second.getHeight()) {
            return false;
        }
        for (int y = 0; y < first.getHeight(); y++) {
            for (int x = 0; x < first.getWidth(); x++) {
                if (first.get(x, y) != second.get(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
