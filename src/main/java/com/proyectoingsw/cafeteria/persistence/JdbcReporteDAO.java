package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Reporte;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public final class JdbcReporteDAO extends JdbcDataHelper<Reporte, String> implements ReporteDAO {
    private static final String COLUMNS =
            "ID_REPORTE, FECHA_GENERACION, FRANJA_HORARIA, GRAFICOS, DESCRIPCION";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM REPORTE ORDER BY FECHA_GENERACION, ID_REPORTE";

    public JdbcReporteDAO(JdbcDatabase database) {
        super(
                database,
                SELECT_ALL,
                "SELECT " + COLUMNS + " FROM REPORTE WHERE ID_REPORTE = ?",
                "INSERT INTO REPORTE (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?)",
                "UPDATE REPORTE SET FECHA_GENERACION = ?, FRANJA_HORARIA = ?, "
                        + "GRAFICOS = ?, DESCRIPCION = ? WHERE ID_REPORTE = ?",
                "DELETE FROM REPORTE WHERE ID_REPORTE = ?",
                JdbcReporteDAO::map,
                (statement, reporte) -> {
                    statement.setString(1, reporte.idReporte());
                    statement.setDate(2, Date.valueOf(reporte.fechaGeneracion()));
                    statement.setString(3, reporte.franjaHoraria());
                    statement.setBoolean(4, reporte.graficos());
                    statement.setString(5, reporte.descripcion());
                },
                (statement, reporte) -> {
                    statement.setDate(1, Date.valueOf(reporte.fechaGeneracion()));
                    statement.setString(2, reporte.franjaHoraria());
                    statement.setBoolean(3, reporte.graficos());
                    statement.setString(4, reporte.descripcion());
                    statement.setString(5, reporte.idReporte());
                },
                (statement, id) -> statement.setString(1, id));
    }

    @Override
    public List<Reporte> buscarPorFecha(java.time.LocalDate fechaGeneracion) {
        Objects.requireNonNull(fechaGeneracion);
        return query(
                "SELECT " + COLUMNS + " FROM REPORTE WHERE FECHA_GENERACION = ? "
                        + "ORDER BY ID_REPORTE",
                (statement, ignored) -> statement.setDate(
                        1, Date.valueOf(fechaGeneracion)));
    }

    @Override
    public List<Reporte> buscarPorFranjaHoraria(String franjaHoraria) {
        Objects.requireNonNull(franjaHoraria);
        return query(
                "SELECT " + COLUMNS + " FROM REPORTE WHERE FRANJA_HORARIA = ? "
                        + "ORDER BY FECHA_GENERACION, ID_REPORTE",
                (statement, ignored) -> statement.setString(1, franjaHoraria));
    }

    private static Reporte map(ResultSet resultSet) throws SQLException {
        return new Reporte(
                resultSet.getString("ID_REPORTE"),
                resultSet.getDate("FECHA_GENERACION").toLocalDate(),
                resultSet.getString("FRANJA_HORARIA"),
                resultSet.getBoolean("GRAFICOS"),
                resultSet.getString("DESCRIPCION"));
    }
}
