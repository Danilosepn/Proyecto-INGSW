package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.application.AdministradorFAQ;
import com.proyectoingsw.cafeteria.application.GestorResenia;
import com.proyectoingsw.cafeteria.domain.PreguntaFAQ;
import java.util.List;
import java.util.Objects;

public final class PantallaServicioCliente {
    private final VistaFAQ faq;
    private final FormularioResenia formularioResenia;
    private final AdministradorFAQ administracionFAQ;
    private final GestorResenia gestionResenia;

    public PantallaServicioCliente(
            AdministradorFAQ administracionFAQ,
            GestorResenia gestionResenia) {
        this.faq = new VistaFAQ(List.<PreguntaFAQ>of(), "");
        this.formularioResenia = new FormularioResenia("");
        this.administracionFAQ = Objects.requireNonNull(administracionFAQ);
        this.gestionResenia = Objects.requireNonNull(gestionResenia);
    }

    public AdministradorFAQ getAdministracionFAQ() {
        return administracionFAQ;
    }

    public GestorResenia getGestionResenia() {
        return gestionResenia;
    }
}
