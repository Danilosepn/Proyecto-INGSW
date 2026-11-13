package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Mesa;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class JdbcMesaDAO extends JdbcDataHelper<Mesa, Integer> implements MesaDAO {
    private static final String COLUMNS = "ID_MESA, RESERVADA";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM MESA ORDER BY ID_MESA";

    public JdbcMesaDAO(JdbcDatabase database) {
        super(
                database,
                SELECT_ALL,
                "SELECT " + COLUMNS + " FROM MESA WHERE ID_MESA = ?",
                "INSERT INTO MESA (" + COLUMNS + ") VALUES (?, ?)",
                "UPDATE MESA SET RESERVADA = ? WHERE ID_MESA = ?",
                "DELETE FROM MESA WHERE ID_MESA = ?",
                JdbcMesaDAO::map,
                (statement, mesa) -> {
                    statement.setInt(1, mesa.idMesa());
                    statement.setBoolean(2, mesa.reservada());
                },
                (statement, mesa) -> {
                    statement.setBoolean(1, mesa.reservada());
                    statement.setInt(2, mesa.idMesa());
                },
                (statement, id) -> statement.setInt(1, id));
    }

    @Override
    public List<Mesa> buscarPorEstado(boolean reservada) {
        return query(
                "SELECT " + COLUMNS + " FROM MESA WHERE RESERVADA = ? ORDER BY ID_MESA",
                (statement, ignored) -> statement.setBoolean(1, reservada));
    }

    private static Mesa map(ResultSet resultSet) throws SQLException {
        return new Mesa(resultSet.getInt("ID_MESA"), resultSet.getBoolean("RESERVADA"));
    }

}
