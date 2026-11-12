package com.proyectoingsw.cafeteria.ui.boundary;

import java.util.Objects;

public final class MenuPrincipal {
    private final SelectorVentanas selectorVentanas;
    private boolean visible;

    public MenuPrincipal(SelectorVentanas selectorVentanas) {
        this.selectorVentanas = Objects.requireNonNull(selectorVentanas);
    }

    public SelectorVentanas getSelectorVentanas() {
        return selectorVentanas;
    }

    public void mostrar() {
        visible = true;
    }

    public void cambiarPantalla(String pantalla) {
        selectorVentanas.cambiarVentana(pantalla);
    }

    public boolean isVisible() {
        return visible;
    }
}
