package com.proyectoingsw.cafeteria.application;

public interface PagarTarjetaOTransferencia {
    boolean pagarConTransferencia();

    boolean pagarConTarjeta();

    void generarPagoPorPayPhone();
}
