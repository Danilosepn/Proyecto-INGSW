package com.proyectoingsw.cafeteria.persistence;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class InMemoryDataHelperSQL<T, K> implements IDataHelperSQL<T, K> {
    private final Map<K, T> entities = new LinkedHashMap<>();
    private final Function<T, K> keyExtractor;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public InMemoryDataHelperSQL(Function<T, K> keyExtractor) {
        this.keyExtractor = Objects.requireNonNull(keyExtractor);
    }

    @Override
    public void agregar(T object) {
        Objects.requireNonNull(object);
        K key = Objects.requireNonNull(keyExtractor.apply(object));
        lock.writeLock().lock();
        try {
            if (entities.containsKey(key)) {
                throw new IllegalStateException("An entity with this identifier already exists: " + key);
            }
            entities.put(key, object);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public T buscarPorId(K id) {
        Objects.requireNonNull(id);
        lock.readLock().lock();
        try {
            T entity = entities.get(id);
            if (entity == null) {
                throw new NoSuchElementException("No entity exists with identifier: " + id);
            }
            return entity;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<T> leerTodos() {
        lock.readLock().lock();
        try {
            return List.copyOf(entities.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean actualizar(T object) {
        Objects.requireNonNull(object);
        K key = Objects.requireNonNull(keyExtractor.apply(object));
        lock.writeLock().lock();
        try {
            if (!entities.containsKey(key)) {
                return false;
            }
            entities.put(key, object);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public T eliminar(K id) {
        Objects.requireNonNull(id);
        lock.writeLock().lock();
        try {
            T removed = entities.remove(id);
            if (removed == null) {
                throw new NoSuchElementException("No entity exists with identifier: " + id);
            }
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
