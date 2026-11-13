package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.LineaFactura;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class JdbcLineaFacturaDAO extends JdbcDataHelper<LineaFactura, Integer>
        implements LineaFacturaDAO {
    private static final String COLUMNS =
            "NUM_LINEA_COMPRA, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL_ALIMENTO";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM LINEA_FACTURA ORDER BY NUM_LINEA_COMPRA";

    public JdbcLineaFacturaDAO(JdbcDatabase database) {
        super(
                database,
                SELECT_ALL,
                "SELECT " + COLUMNS + " FROM LINEA_FACTURA WHERE NUM_LINEA_COMPRA = ?",
                "INSERT INTO LINEA_FACTURA (" + COLUMNS + ") VALUES (?, ?, ?, ?)",
                "UPDATE LINEA_FACTURA SET CANTIDAD = ?, PRECIO_UNITARIO = ?, "
                        + "SUBTOTAL_ALIMENTO = ? WHERE NUM_LINEA_COMPRA = ?",
                "DELETE FROM LINEA_FACTURA WHERE NUM_LINEA_COMPRA = ?",
                JdbcLineaFacturaDAO::map,
                (statement, linea) -> {
                    statement.setInt(1, linea.numLineaCompra());
                    statement.setInt(2, linea.cantidad());
                    statement.setBigDecimal(3, linea.precioUnitario());
                    statement.setBigDecimal(4, linea.subtotalAlimento());
                },
                (statement, linea) -> {
                    statement.setInt(1, linea.cantidad());
                    statement.setBigDecimal(2, linea.precioUnitario());
                    statement.setBigDecimal(3, linea.subtotalAlimento());
                    statement.setInt(4, linea.numLineaCompra());
                },
                (statement, id) -> statement.setInt(1, id));
    }

    private static LineaFactura map(ResultSet resultSet) throws SQLException {
        return new LineaFactura(
                resultSet.getInt("NUM_LINEA_COMPRA"),
                resultSet.getInt("CANTIDAD"),
                resultSet.getBigDecimal("PRECIO_UNITARIO"),
                resultSet.getBigDecimal("SUBTOTAL_ALIMENTO"));
    }
}
