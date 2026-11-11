package com.proyectoingsw.cafeteria.ui;

import com.proyectoingsw.cafeteria.domain.PreguntaFAQ;
import java.util.List;

public final class VistaFAQ {
    private List<PreguntaFAQ> faqs;
    private String tema;
    private boolean visible;

    public VistaFAQ(List<PreguntaFAQ> faqs, String tema) {
        this.faqs = List.copyOf(faqs);
        this.tema = tema;
    }

    public List<PreguntaFAQ> getFaqs() {
        return faqs;
    }

    public String getTema() {
        return tema;
    }

    public void actualizar(List<PreguntaFAQ> faqs, String tema) {
        this.faqs = List.copyOf(faqs);
        this.tema = tema;
    }

    public void mostrar() {
        visible = true;
    }

    public void ocultar() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }
}
