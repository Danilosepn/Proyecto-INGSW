package com.proyectoingsw.cafeteria.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class JdbcReservaMesaDAO {
    private final JdbcDatabase database;

    public JdbcReservaMesaDAO(JdbcDatabase database) {
        this.database = Objects.requireNonNull(database);
    }

    public void asociar(int idReserva, int idMesa) {
        database.inTransaction(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT RESERVADA FROM MESA WHERE ID_MESA = ? FOR UPDATE")) {
                statement.setInt(1, idMesa);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new IllegalArgumentException("Mesa does not exist: " + idMesa);
                    }
                    if (resultSet.getBoolean(1)) {
                        throw new IllegalStateException("Mesa is already reserved: " + idMesa);
                    }
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO RESERVA_MESA (ID_RESERVA, ID_MESA) VALUES (?, ?)")) {
                statement.setInt(1, idReserva);
                statement.setInt(2, idMesa);
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE MESA SET RESERVADA = TRUE WHERE ID_MESA = ?")) {
                statement.setInt(1, idMesa);
                if (statement.executeUpdate() != 1) {
                    throw new SQLException("Mesa disappeared during reservation: " + idMesa);
                }
            }
            return null;
        });
    }

    public void desasociar(int idReserva, int idMesa) {
        database.inTransaction(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM RESERVA_MESA WHERE ID_RESERVA = ? AND ID_MESA = ?")) {
                statement.setInt(1, idReserva);
                statement.setInt(2, idMesa);
                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException(
                            "Reservation-table association does not exist.");
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE MESA SET RESERVADA = FALSE WHERE ID_MESA = ?")) {
                statement.setInt(1, idMesa);
                if (statement.executeUpdate() != 1) {
                    throw new SQLException("Mesa disappeared during reservation release: " + idMesa);
                }
            }
            return null;
        });
    }
}
