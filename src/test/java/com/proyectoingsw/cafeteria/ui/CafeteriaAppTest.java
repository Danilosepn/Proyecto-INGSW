package com.proyectoingsw.cafeteria.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.proyectoingsw.cafeteria.ui.boundary.PantallaBase;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class CafeteriaAppTest {
    @Test
    void createsAllNavigableScreensWithoutOpeningAWindow() throws Exception {
        AtomicReference<Map<String, PantallaBase>> screens = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> screens.set(CafeteriaApp.crearPantallas()));

        assertEquals(Set.of(
                "menu", "comedor", "mesas", "pago", "ticket",
                "cajero", "estadisticas", "login"), screens.get().keySet());
    }
}
