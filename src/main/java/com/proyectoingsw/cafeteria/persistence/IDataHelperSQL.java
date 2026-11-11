package com.proyectoingsw.cafeteria.persistence;

import java.util.List;

public interface IDataHelperSQL<T, K> {
    void agregar(T object);

    T buscarPorId(K id);

    List<T> leerTodos();

    boolean actualizar(T object);

    T eliminar(K id);
}
