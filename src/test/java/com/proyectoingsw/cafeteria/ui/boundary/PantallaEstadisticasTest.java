package com.proyectoingsw.cafeteria.ui.boundary;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.proyectoingsw.cafeteria.application.demo.DemoCafeteriaServices;
import com.proyectoingsw.cafeteria.domain.Mesa;
import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class PantallaEstadisticasTest {
    @Test
    void seleccionMesaReturnsTheMesaChosenInTheStatisticsScreen() throws Exception {
        Mesa comedor = new Mesa(1, false);
        Mesa terraza = new Mesa(2, false);
        AtomicReference<PantallaEstadisticas> pantalla = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            DemoCafeteriaServices services = new DemoCafeteriaServices();
            PantallaEstadisticas estadisticas =
                    new PantallaEstadisticas(services, services, List.of(comedor, terraza));
            JComboBox<?> selector = findMesaSelector(estadisticas);
            assertNotNull(selector);
            selector.setSelectedIndex(1);
            pantalla.set(estadisticas);
        });

        assertSame(terraza, pantalla.get().seleccionMesa());
    }

    private static JComboBox<?> findMesaSelector(Container parent) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JComboBox<?> comboBox) {
                return comboBox;
            }
            if (component instanceof Container child) {
                JComboBox<?> selector = findMesaSelector(child);
                if (selector != null) {
                    return selector;
                }
            }
        }
        return null;
    }
}
