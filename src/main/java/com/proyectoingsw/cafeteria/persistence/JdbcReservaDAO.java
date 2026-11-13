package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Reserva;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public final class JdbcReservaDAO extends JdbcDataHelper<Reserva, Integer>
        implements ReservaDAO {
    private static final String COLUMNS =
            "ID_RESERVA, FECHA_CREACION, FRANJA_HORARIA, CODIGO_ESTUDIANTE, CEDULA_PROFESOR";
    private static final String SELECT_ALL =
            "SELECT " + COLUMNS + " FROM RESERVA ORDER BY FECHA_CREACION, ID_RESERVA";

    public JdbcReservaDAO(JdbcDatabase database) {
        super(
                database,
                SELECT_ALL,
                "SELECT " + COLUMNS + " FROM RESERVA WHERE ID_RESERVA = ?",
                "INSERT INTO RESERVA (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?)",
                "UPDATE RESERVA SET FECHA_CREACION = ?, FRANJA_HORARIA = ?, "
                        + "CODIGO_ESTUDIANTE = ?, CEDULA_PROFESOR = ? WHERE ID_RESERVA = ?",
                "DELETE FROM RESERVA WHERE ID_RESERVA = ?",
                JdbcReservaDAO::map,
                (statement, reserva) -> {
                    statement.setInt(1, reserva.idReserva());
                    statement.setDate(2, Date.valueOf(reserva.fechaCreacion()));
                    statement.setString(3, reserva.franjaHoraria());
                    statement.setString(4, reserva.codigoEstudiante());
                    statement.setString(5, reserva.cedulaProfesor());
                },
                (statement, reserva) -> {
                    statement.setDate(1, Date.valueOf(reserva.fechaCreacion()));
                    statement.setString(2, reserva.franjaHoraria());
                    statement.setString(3, reserva.codigoEstudiante());
                    statement.setString(4, reserva.cedulaProfesor());
                    statement.setInt(5, reserva.idReserva());
                },
                (statement, id) -> statement.setInt(1, id));
    }

    @Override
    public List<Reserva> buscarPorFecha(java.time.LocalDate fechaCreacion) {
        Objects.requireNonNull(fechaCreacion);
        return query(
                "SELECT " + COLUMNS + " FROM RESERVA WHERE FECHA_CREACION = ? "
                        + "ORDER BY ID_RESERVA",
                (statement, ignored) -> statement.setDate(
                        1, Date.valueOf(fechaCreacion)));
    }

    @Override
    public List<Reserva> buscarPorEstudiante(String codigoUnico) {
        Objects.requireNonNull(codigoUnico);
        return query(
                "SELECT " + COLUMNS + " FROM RESERVA WHERE CODIGO_ESTUDIANTE = ? "
                        + "ORDER BY FECHA_CREACION, ID_RESERVA",
                (statement, ignored) -> statement.setString(1, codigoUnico));
    }

    @Override
    public List<Reserva> buscarPorFranjaHoraria(String franjaHoraria) {
        Objects.requireNonNull(franjaHoraria);
        return query(
                "SELECT " + COLUMNS + " FROM RESERVA WHERE FRANJA_HORARIA = ? "
                        + "ORDER BY FECHA_CREACION, ID_RESERVA",
                (statement, ignored) -> statement.setString(1, franjaHoraria));
    }

    @Override
    public List<Reserva> buscarPorProfesor(String cedula) {
        Objects.requireNonNull(cedula);
        return query(
                "SELECT " + COLUMNS + " FROM RESERVA WHERE CEDULA_PROFESOR = ? "
                        + "ORDER BY FECHA_CREACION, ID_RESERVA",
                (statement, ignored) -> statement.setString(1, cedula));
    }

    private static Reserva map(ResultSet resultSet) throws SQLException {
        return new Reserva(
                resultSet.getInt("ID_RESERVA"),
                resultSet.getDate("FECHA_CREACION").toLocalDate(),
                resultSet.getString("FRANJA_HORARIA"),
                resultSet.getString("CODIGO_ESTUDIANTE"),
                resultSet.getString("CEDULA_PROFESOR"));
    }
}
