package com.proyectoingsw.cafeteria.application;

public interface PagarEfectivo {
    boolean pagarConEfectivo();

    int calcularVuelto();

    String generarRecibo();
}
