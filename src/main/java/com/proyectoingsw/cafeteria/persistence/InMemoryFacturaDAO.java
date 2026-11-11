package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Factura;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class InMemoryFacturaDAO extends InMemoryDataHelperSQL<Factura, String>
        implements FacturaDAO {
    public InMemoryFacturaDAO() {
        super(Factura::idFactura);
    }

    @Override
    public List<Factura> buscarPorMetodoPago(String metodo) {
        Objects.requireNonNull(metodo);
        return leerTodos().stream().filter(factura -> metodo.equals(factura.metodoPago())).toList();
    }

    @Override
    public List<Factura> buscarPorFecha(LocalDate fecha) {
        Objects.requireNonNull(fecha);
        return leerTodos().stream().filter(factura -> fecha.equals(factura.fecha())).toList();
    }
}
