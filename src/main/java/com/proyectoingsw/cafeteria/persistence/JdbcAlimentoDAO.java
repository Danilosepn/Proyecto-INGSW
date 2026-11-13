package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Alimento;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public final class JdbcAlimentoDAO extends JdbcDataHelper<Alimento, Integer>
        implements AlimentoDAO {
    private static final String COLUMNS =
            "ID_ALIMENTO, NOMBRE, TIPO, DESCRIPCION, CANTIDAD_EN_STOCK, PRECIO_UNITARIO";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM ALIMENTO ORDER BY ID_ALIMENTO";

    public JdbcAlimentoDAO(JdbcDatabase database) {
        super(
                database,
                SELECT_ALL,
                "SELECT " + COLUMNS + " FROM ALIMENTO WHERE ID_ALIMENTO = ?",
                "INSERT INTO ALIMENTO (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?)",
                "UPDATE ALIMENTO SET NOMBRE = ?, TIPO = ?, DESCRIPCION = ?, "
                        + "CANTIDAD_EN_STOCK = ?, PRECIO_UNITARIO = ? WHERE ID_ALIMENTO = ?",
                "DELETE FROM ALIMENTO WHERE ID_ALIMENTO = ?",
                JdbcAlimentoDAO::map,
                JdbcAlimentoDAO::bindInsert,
                JdbcAlimentoDAO::bindUpdate,
                (statement, id) -> statement.setInt(1, id));
    }

    @Override
    public List<Alimento> buscarPorTipo(String categoria) {
        Objects.requireNonNull(categoria);
        return query(
                "SELECT " + COLUMNS + " FROM ALIMENTO WHERE TIPO = ? ORDER BY NOMBRE",
                (statement, ignored) -> statement.setString(1, categoria));
    }

    private static Alimento map(ResultSet resultSet) throws SQLException {
        return new Alimento(
                resultSet.getInt("ID_ALIMENTO"),
                resultSet.getString("NOMBRE"),
                resultSet.getString("TIPO"),
                resultSet.getString("DESCRIPCION"),
                resultSet.getInt("CANTIDAD_EN_STOCK"),
                resultSet.getBigDecimal("PRECIO_UNITARIO"));
    }

    private static void bindInsert(PreparedStatement statement, Alimento alimento)
            throws SQLException {
        statement.setInt(1, alimento.idAlimento());
        statement.setString(2, alimento.nombre());
        statement.setString(3, alimento.tipo());
        statement.setString(4, alimento.descripcion());
        statement.setInt(5, alimento.cantidadEnStock());
        statement.setBigDecimal(6, alimento.precioUnitario());
    }

    private static void bindUpdate(PreparedStatement statement, Alimento alimento)
            throws SQLException {
        statement.setString(1, alimento.nombre());
        statement.setString(2, alimento.tipo());
        statement.setString(3, alimento.descripcion());
        statement.setInt(4, alimento.cantidadEnStock());
        statement.setBigDecimal(5, alimento.precioUnitario());
        statement.setInt(6, alimento.idAlimento());
    }
}
