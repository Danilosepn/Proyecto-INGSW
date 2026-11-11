package com.proyectoingsw.cafeteria.persistence;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class ConnectionDBTest {
    @Test
    void reportsAnUnavailableConfiguredDriver() {
        ConnectionDB database = new ConnectionDB(
                "jdbc:unknown:test",
                "missing.jdbc.Driver",
                "user",
                "password");

        assertThrows(SQLException.class, database::connect);
    }
}
