package com.proyectoingsw.cafeteria.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class JdbcDataHelper<T, K> implements IDataHelperSQL<T, K> {
    @FunctionalInterface
    protected interface RowMapper<T> {
        T map(ResultSet resultSet) throws SQLException;
    }

    @FunctionalInterface
    protected interface Binder<T> {
        void bind(PreparedStatement statement, T value) throws SQLException;
    }

    @FunctionalInterface
    protected interface KeyBinder<K> {
        void bind(PreparedStatement statement, K key) throws SQLException;
    }

    private final JdbcDatabase database;
    private final String selectAll;
    private final String selectById;
    private final String insert;
    private final String update;
    private final String delete;
    private final RowMapper<T> rowMapper;
    private final Binder<T> insertBinder;
    private final Binder<T> updateBinder;
    private final KeyBinder<K> keyBinder;

    protected JdbcDataHelper(
            JdbcDatabase database,
            String selectAll,
            String selectById,
            String insert,
            String update,
            String delete,
            RowMapper<T> rowMapper,
            Binder<T> insertBinder,
            Binder<T> updateBinder,
            KeyBinder<K> keyBinder) {
        this.database = Objects.requireNonNull(database);
        this.selectAll = selectAll;
        this.selectById = selectById;
        this.insert = insert;
        this.update = update;
        this.delete = delete;
        this.rowMapper = Objects.requireNonNull(rowMapper);
        this.insertBinder = Objects.requireNonNull(insertBinder);
        this.updateBinder = Objects.requireNonNull(updateBinder);
        this.keyBinder = Objects.requireNonNull(keyBinder);
    }

    @Override
    public void agregar(T object) {
        Objects.requireNonNull(object);
        database.withConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(insert)) {
                insertBinder.bind(statement, object);
                statement.executeUpdate();
            }
            return null;
        });
    }

    @Override
    public T buscarPorId(K id) {
        Objects.requireNonNull(id);
        return database.withConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(selectById)) {
                keyBinder.bind(statement, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new NoSuchElementException("No entity exists with identifier: " + id);
                    }
                    return rowMapper.map(resultSet);
                }
            }
        });
    }

    @Override
    public List<T> leerTodos() {
        return query(selectAll, (statement, ignored) -> { });
    }

    @Override
    public boolean actualizar(T object) {
        Objects.requireNonNull(object);
        return database.withConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(update)) {
                updateBinder.bind(statement, object);
                return statement.executeUpdate() == 1;
            }
        });
    }

    @Override
    public T eliminar(K id) {
        Objects.requireNonNull(id);
        return database.inTransaction(connection -> {
            T entity;
            try (PreparedStatement statement = connection.prepareStatement(selectById)) {
                keyBinder.bind(statement, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new NoSuchElementException(
                                "No entity exists with identifier: " + id);
                    }
                    entity = rowMapper.map(resultSet);
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(delete)) {
                keyBinder.bind(statement, id);
                statement.executeUpdate();
            }
            return entity;
        });
    }

    protected final List<T> query(String sql, Binder<Void> parameters) {
        return database.withConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                parameters.bind(statement, null);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<T> entities = new ArrayList<>();
                    while (resultSet.next()) {
                        entities.add(rowMapper.map(resultSet));
                    }
                    return List.copyOf(entities);
                }
            }
        });
    }

    protected final JdbcDatabase database() {
        return database;
    }
}
