package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Factura;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public final class JdbcFacturaDAO extends JdbcDataHelper<Factura, String> implements FacturaDAO {
    private static final String COLUMNS =
            "ID_FACTURA, FECHA, NUMERO_PEDIDO, METODO_PAGO, TOTAL, DESCUENTO";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM FACTURA ORDER BY FECHA, ID_FACTURA";

    public JdbcFacturaDAO(JdbcDatabase database) {
        super(
                database,
                SELECT_ALL,
                "SELECT " + COLUMNS + " FROM FACTURA WHERE ID_FACTURA = ?",
                "INSERT INTO FACTURA (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?)",
                "UPDATE FACTURA SET FECHA = ?, NUMERO_PEDIDO = ?, METODO_PAGO = ?, "
                        + "TOTAL = ?, DESCUENTO = ? WHERE ID_FACTURA = ?",
                "DELETE FROM FACTURA WHERE ID_FACTURA = ?",
                JdbcFacturaDAO::map,
                (statement, factura) -> {
                    statement.setString(1, factura.idFactura());
                    statement.setDate(2, Date.valueOf(factura.fecha()));
                    statement.setInt(3, factura.numeroPedido());
                    statement.setString(4, factura.metodoPago());
                    statement.setBigDecimal(5, factura.total());
                    statement.setBigDecimal(6, factura.descuento());
                },
                (statement, factura) -> {
                    statement.setDate(1, Date.valueOf(factura.fecha()));
                    statement.setInt(2, factura.numeroPedido());
                    statement.setString(3, factura.metodoPago());
                    statement.setBigDecimal(4, factura.total());
                    statement.setBigDecimal(5, factura.descuento());
                    statement.setString(6, factura.idFactura());
                },
                (statement, id) -> statement.setString(1, id));
    }

    @Override
    public List<Factura> buscarPorMetodoPago(String metodo) {
        Objects.requireNonNull(metodo);
        return query(
                "SELECT " + COLUMNS + " FROM FACTURA WHERE METODO_PAGO = ? "
                        + "ORDER BY FECHA, ID_FACTURA",
                (statement, ignored) -> statement.setString(1, metodo));
    }

    @Override
    public List<Factura> buscarPorFecha(java.time.LocalDate fecha) {
        Objects.requireNonNull(fecha);
        return query(
                "SELECT " + COLUMNS + " FROM FACTURA WHERE FECHA = ? ORDER BY ID_FACTURA",
                (statement, ignored) -> statement.setDate(1, Date.valueOf(fecha)));
    }

    private static Factura map(ResultSet resultSet) throws SQLException {
        return new Factura(
                resultSet.getString("ID_FACTURA"),
                resultSet.getDate("FECHA").toLocalDate(),
                resultSet.getInt("NUMERO_PEDIDO"),
                resultSet.getString("METODO_PAGO"),
                resultSet.getBigDecimal("TOTAL"),
                resultSet.getBigDecimal("DESCUENTO"));
    }
}
