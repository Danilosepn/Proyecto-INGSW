package com.proyectoingsw.cafeteria.ui;

public final class FormularioPago {
    private String opcionPago;
    private String datosPago;

    public FormularioPago(String opcionPago, String datosPago) {
        this.opcionPago = opcionPago;
        this.datosPago = datosPago;
    }

    public String obtenerOpcionPago() {
        return opcionPago;
    }

    public String obtenerDatosPago() {
        return datosPago;
    }

    public void actualizar(String opcionPago, String datosPago) {
        this.opcionPago = opcionPago;
        this.datosPago = datosPago;
    }
}
