package com.proyectoingsw.cafeteria.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public final class JdbcDatabase {
    private static final String SCHEMA_RESOURCE = "/db/schema.sql";

    @FunctionalInterface
    public interface SqlWork<T> {
        T apply(Connection connection) throws SQLException;
    }

    private final String url;
    private final String user;
    private final String password;

    public JdbcDatabase(String url, String user, String password) {
        this.url = Objects.requireNonNull(url);
        this.user = Objects.requireNonNull(user);
        this.password = Objects.requireNonNull(password);
        initializeSchema();
    }

    public static JdbcDatabase fileBackedDefault() {
        return new JdbcDatabase(
                "jdbc:h2:file:./data/cafeteria;DB_CLOSE_ON_EXIT=FALSE",
                "sa",
                "");
    }

    public <T> T withConnection(SqlWork<T> work) {
        Objects.requireNonNull(work);
        try (Connection connection = openConnection()) {
            return work.apply(connection);
        } catch (SQLException exception) {
            throw new PersistenceException("Database operation failed.", exception);
        }
    }

    public <T> T inTransaction(SqlWork<T> work) {
        Objects.requireNonNull(work);
        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);
            try {
                T result = work.apply(connection);
                connection.commit();
                return result;
            } catch (SQLException | RuntimeException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackFailure) {
                    exception.addSuppressed(rollbackFailure);
                }
                if (exception instanceof SQLException sqlException) {
                    throw new PersistenceException("Database transaction failed.", sqlException);
                }
                throw exception;
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Could not manage database transaction.", exception);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void initializeSchema() {
        String schema = readSchema();
        withConnection(connection -> {
            try (Statement statement = connection.createStatement()) {
                for (String command : schema.split(";")) {
                    if (!command.isBlank()) {
                        statement.execute(command);
                    }
                }
            }
            return null;
        });
    }

    private static String readSchema() {
        try (InputStream stream = JdbcDatabase.class.getResourceAsStream(SCHEMA_RESOURCE)) {
            if (stream == null) {
                throw new IllegalStateException("Missing database schema resource: " + SCHEMA_RESOURCE);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read database schema.", exception);
        }
    }
}
