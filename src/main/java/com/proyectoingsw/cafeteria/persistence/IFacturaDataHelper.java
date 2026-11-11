package com.proyectoingsw.cafeteria.persistence;

import com.proyectoingsw.cafeteria.domain.Factura;
import java.time.LocalDate;
import java.util.List;

public interface IFacturaDataHelper {
    List<Factura> buscarPorMetodoPago(String metodo);

    List<Factura> buscarPorFecha(LocalDate fecha);
}
