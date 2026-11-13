package com.proyectoingsw.cafeteria.persistence;

import com.google.zxing.common.BitMatrix;
import com.proyectoingsw.cafeteria.domain.Ticket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public final class JdbcTicketDAO extends JdbcDataHelper<Ticket, Integer> implements TicketDAO {
    private static final String COLUMNS = "ID_TICKET, FECHA_GENERACION, VALIDO, CODIGO_QR";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM TICKET ORDER BY ID_TICKET";

    public JdbcTicketDAO(JdbcDatabase database) {
        super(
                database,
                SELECT_ALL,
                "SELECT " + COLUMNS + " FROM TICKET WHERE ID_TICKET = ?",
                "INSERT INTO TICKET (" + COLUMNS + ") VALUES (?, ?, ?, ?)",
                "UPDATE TICKET SET FECHA_GENERACION = ?, VALIDO = ?, CODIGO_QR = ? "
                        + "WHERE ID_TICKET = ?",
                "DELETE FROM TICKET WHERE ID_TICKET = ?",
                JdbcTicketDAO::map,
                (statement, ticket) -> {
                    statement.setInt(1, ticket.idTicket());
                    statement.setTimestamp(2, Timestamp.valueOf(ticket.fechaGeneracion()));
                    statement.setBoolean(3, ticket.valido());
                    statement.setBytes(4, encode(ticket.codigoQR()));
                },
                (statement, ticket) -> {
                    statement.setTimestamp(1, Timestamp.valueOf(ticket.fechaGeneracion()));
                    statement.setBoolean(2, ticket.valido());
                    statement.setBytes(3, encode(ticket.codigoQR()));
                    statement.setInt(4, ticket.idTicket());
                },
                (statement, id) -> statement.setInt(1, id));
    }

    @Override
    public List<Ticket> buscarPorValido(boolean valido) {
        return query(
                "SELECT " + COLUMNS + " FROM TICKET WHERE VALIDO = ? ORDER BY ID_TICKET",
                (statement, ignored) -> statement.setBoolean(1, valido));
    }

    private static Ticket map(ResultSet resultSet) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp("FECHA_GENERACION");
        return new Ticket(
                resultSet.getInt("ID_TICKET"),
                timestamp.toLocalDateTime(),
                resultSet.getBoolean("VALIDO"),
                decode(resultSet.getBytes("CODIGO_QR")));
    }

    private static byte[] encode(BitMatrix matrix) throws SQLException {
        if (matrix == null) {
            return null;
        }
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (DataOutputStream output = new DataOutputStream(bytes)) {
                output.writeInt(matrix.getWidth());
                output.writeInt(matrix.getHeight());
                for (int y = 0; y < matrix.getHeight(); y++) {
                    for (int x = 0; x < matrix.getWidth(); x++) {
                        output.writeBoolean(matrix.get(x, y));
                    }
                }
            }
            return bytes.toByteArray();
        } catch (IOException exception) {
            throw new SQLException("Could not encode ticket QR matrix.", exception);
        }
    }

    private static BitMatrix decode(byte[] bytes) throws SQLException {
        if (bytes == null) {
            return null;
        }
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes))) {
            int width = input.readInt();
            int height = input.readInt();
            if (width <= 0 || height <= 0 || (long) width * height > 4_000_000L) {
                throw new SQLException("Stored ticket QR matrix has invalid dimensions.");
            }
            BitMatrix matrix = new BitMatrix(width, height);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (input.readBoolean()) {
                        matrix.set(x, y);
                    }
                }
            }
            if (input.read() != -1) {
                throw new SQLException("Stored ticket QR matrix contains trailing data.");
            }
            return matrix;
        } catch (IOException exception) {
            throw new SQLException("Could not decode ticket QR matrix.", exception);
        }
    }
}
