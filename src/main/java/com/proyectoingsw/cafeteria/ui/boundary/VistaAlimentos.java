package com.proyectoingsw.cafeteria.ui.boundary;

import com.proyectoingsw.cafeteria.domain.Alimento;
import java.util.List;

public final class VistaAlimentos {
    private List<Alimento> menuAlimentos;
    private boolean visible;

    public VistaAlimentos(List<Alimento> menuAlimentos) {
        this.menuAlimentos = List.copyOf(menuAlimentos);
    }

    public List<Alimento> getMenuAlimentos() {
        return menuAlimentos;
    }

    public void actualizarAlimentos(List<Alimento> alimentos) {
        menuAlimentos = List.copyOf(alimentos);
    }

    public void ocultar() {
        visible = false;
    }

    public void mostrar() {
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }
}
