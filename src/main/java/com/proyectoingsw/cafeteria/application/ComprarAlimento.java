package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Compra;

public interface ComprarAlimento {
    Compra iniciarCompra();

    String verCarritoCompra(Compra compra);

    void leerCuponDescuento(String codigo, double descuento);
}
