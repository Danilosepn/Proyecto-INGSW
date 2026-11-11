package com.proyectoingsw.cafeteria.application;

import com.proyectoingsw.cafeteria.domain.Factura;
import org.w3c.dom.Document;

public interface GestorFactura {
    Factura generarFactura();

    String imprimirFacturaVirtual();

    Document convertirAPDF(Factura factura);
}
