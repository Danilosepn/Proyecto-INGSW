package com.proyectoingsw.cafeteria.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public final class JdbcFacturaTicketDAO {
    private final JdbcDatabase database;

    public JdbcFacturaTicketDAO(JdbcDatabase database) {
        this.database = Objects.requireNonNull(database);
    }

    public void asociar(String idFactura, int idTicket) {
        database.withConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO FACTURA_TICKET (ID_FACTURA, ID_TICKET) VALUES (?, ?)")) {
                statement.setString(1, Objects.requireNonNull(idFactura));
                statement.setInt(2, idTicket);
                statement.executeUpdate();
            }
            return null;
        });
    }

    public int buscarTicket(String idFactura) {
        return database.withConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT ID_TICKET FROM FACTURA_TICKET WHERE ID_FACTURA = ?")) {
                statement.setString(1, Objects.requireNonNull(idFactura));
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new java.util.NoSuchElementException(
                                "Invoice has no associated ticket: " + idFactura);
                    }
                    return resultSet.getInt(1);
                }
            }
        });
    }
}
