package com.proyectoingsw.cafeteria.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.proyectoingsw.cafeteria.ui.boundary.PantallaBase;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaComedor;
import com.proyectoingsw.cafeteria.ui.boundary.PantallaTicket;
import com.proyectoingsw.cafeteria.ui.boundary.MenuPrincipal;
import com.proyectoingsw.cafeteria.ui.boundary.Login;
import com.proyectoingsw.cafeteria.persistence.JdbcAlimentoDAO;
import com.proyectoingsw.cafeteria.persistence.JdbcDatabase;
import com.proyectoingsw.cafeteria.persistence.JdbcMesaDAO;
import com.proyectoingsw.cafeteria.persistence.JdbcTicketDAO;
import java.awt.Component;
import java.awt.Container;
import java.awt.CardLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JPanel;
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

    @Test
    void everyScreenLaysOutAndRendersVisibleControls() throws Exception {
        AtomicReference<Map<String, PantallaBase>> screens = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            Map<String, PantallaBase> allScreens = CafeteriaApp.crearPantallas();
            allScreens.forEach((name, screen) -> {
                screen.setSize(980, 700);
                layoutTree(screen);
                assertTrue(findVisibleButton(screen), name + " should display at least one action");

                BufferedImage image = new BufferedImage(980, 700, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                screen.paint(graphics);
                graphics.dispose();
                int background = screen.getBackground().getRGB() & 0x00ffffff;
                long paintedPixels = 0;
                for (int y = 0; y < image.getHeight(); y += 4) {
                    for (int x = 0; x < image.getWidth(); x += 4) {
                        if ((image.getRGB(x, y) & 0x00ffffff) != background) {
                            paintedPixels++;
                        }
                    }
                }
                assertTrue(paintedPixels > 100, name + " should paint visible content");
            });
            screens.set(allScreens);
        });
        assertFalse(screens.get().isEmpty());
    }

    @Test
    void menuActionsNavigateToTheirRegisteredTargets() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            PantallaBase menu = CafeteriaApp.crearPantallas().get("menu");
            Map<String, String> destinations = Map.of(
                    "Comedor y alimentos", "comedor",
                    "Mesas disponibles", "mesas",
                    "Pago", "pago",
                    "Ticket", "ticket",
                    "Caja", "cajero",
                    "Estadísticas", "estadisticas",
                    "Iniciar sesión", "login");
            destinations.forEach((label, destination) -> {
                AtomicReference<String> selected = new AtomicReference<>();
                menu.setNavegador(selected::set);
                findButton(menu, label).doClick();
                assertEquals(destination, selected.get());
            });
        });
    }

    @Test
    void hitTestingAcrossEveryScreenOnlyActivatesTheControlUnderThePointer() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, PantallaBase> screens = CafeteriaApp.crearPantallas();
            screens.forEach((screenName, screen) -> {
                screen.setSize(980, 700);
                layoutTree(screen);
                var buttons = findVisibleButtons(screen);
                assertFalse(buttons.isEmpty(), screenName + " should expose visible controls");

                Map<Component, Rectangle> bounds = new java.util.IdentityHashMap<>();
                for (JButton button : buttons) {
                    Rectangle buttonBounds = SwingUtilities.convertRectangle(
                            button.getParent(), button.getBounds(), screen);
                    assertTrue(screen.getBounds().contains(buttonBounds),
                            screenName + " button should be within its screen: " + button.getText());
                    Point center = new Point(
                            buttonBounds.x + buttonBounds.width / 2,
                            buttonBounds.y + buttonBounds.height / 2);
                    assertSame(button, SwingUtilities.getDeepestComponentAt(
                            screen, center.x, center.y),
                            screenName + " hit target should match " + button.getText());
                    for (Rectangle other : bounds.values()) {
                        assertFalse(buttonBounds.intersects(other),
                                screenName + " buttons must not overlap");
                    }
                    bounds.put(button, buttonBounds);
                }

                AtomicReference<String> destination = new AtomicReference<>();
                screen.setNavegador(destination::set);
                for (JButton button : buttons) {
                    Point center = SwingUtilities.convertPoint(
                            button, button.getWidth() / 2, button.getHeight() / 2, screen);
                    clickAt(screen, center.x, center.y);
                    assertEquals(expectedNavigation(button, screen), destination.get(),
                            screenName + " click on " + button.getText());
                    destination.set(null);
                }

                for (Point point : List.of(
                        new Point(2, 2),
                        new Point(screen.getWidth() / 2, 2),
                        new Point(screen.getWidth() - 3, 2),
                        new Point(2, screen.getHeight() / 2),
                        new Point(screen.getWidth() - 3, screen.getHeight() / 2),
                        new Point(2, screen.getHeight() - 3),
                        new Point(screen.getWidth() / 2, screen.getHeight() - 3),
                        new Point(screen.getWidth() - 3, screen.getHeight() - 3))) {
                    clickAt(screen, point.x, point.y);
                    Component target = SwingUtilities.getDeepestComponentAt(
                            screen, point.x, point.y);
                    assertEquals(target instanceof JButton button
                                    ? expectedNavigation(button, screen) : null,
                            destination.get(),
                            screenName + " click at " + point);
                    destination.set(null);
                }
            });
        });
    }

    @Test
    void cardLayoutShowsTheRequestedScreenInsteadOfAnEmptyCard() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, PantallaBase> screens = CafeteriaApp.crearPantallas();
            JPanel container = new JPanel(new CardLayout());
            screens.forEach((name, screen) -> container.add(screen, name));
            CardLayout cards = (CardLayout) container.getLayout();
            container.setSize(980, 700);
            container.doLayout();

            for (String name : screens.keySet()) {
                cards.show(container, name);
                assertTrue(screens.get(name).isVisible(), name + " card should be visible");
                screens.forEach((otherName, screen) -> {
                    if (!name.equals(otherName)) {
                        assertFalse(screen.isVisible(),
                                otherName + " should be hidden while " + name + " is active");
                    }
                });
            }
        });
    }

    @Test
    void productionScreensUseFileBackedDaoAdaptersAndSeedOnlyOnce() throws Exception {
        JdbcDatabase database = new JdbcDatabase(
                "jdbc:h2:mem:app_" + UUID.randomUUID().toString().replace("-", "")
                        + ";DB_CLOSE_DELAY=-1",
                "sa",
                "");
        AtomicReference<Map<String, PantallaBase>> screens = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            screens.set(CafeteriaApp.crearPantallas(database));
            CafeteriaApp.crearPantallas(database);
            PantallaComedor comedor = (PantallaComedor) screens.get().get("comedor");
            assertEquals(2, comedor.getMenuAlimentos().size());
            comedor.getFormularioBusqueda().actualizarCriterio("café");
            assertEquals(1, comedor.solicitarBusqueda().size());
            PantallaTicket ticketScreen = (PantallaTicket) screens.get().get("ticket");
            var ticket = ticketScreen.getControlTicket().generarTicket();
            assertTrue(ticketScreen.getControlTicket().validarTicket(ticket));
        });

        assertEquals(3, new JdbcAlimentoDAO(database).leerTodos().size());
        assertEquals(6, new JdbcMesaDAO(database).leerTodos().size());
        assertEquals(1, new JdbcTicketDAO(database).leerTodos().size());
    }

    private static void layoutTree(Container container) {
        container.doLayout();
        for (java.awt.Component child : container.getComponents()) {
            if (child.isVisible()) {
                assertTrue(child.getWidth() > 0 && child.getHeight() > 0,
                        "Visible child should receive non-empty bounds: " + child.getClass());
                if (child instanceof Container nested) {
                    layoutTree(nested);
                }
            }
        }
    }

    private static boolean findVisibleButton(Container container) {
        for (java.awt.Component child : container.getComponents()) {
            if (child instanceof JButton button && button.isVisible()
                    && button.getWidth() > 0 && button.getHeight() > 0) {
                return true;
            }
            if (child instanceof Container nested && findVisibleButton(nested)) {
                return true;
            }
        }
        return false;
    }

    private static java.util.List<JButton> findVisibleButtons(Container container) {
        java.util.List<JButton> buttons = new java.util.ArrayList<>();
        for (Component child : container.getComponents()) {
            if (child instanceof JButton button && button.isVisible()
                    && button.getWidth() > 0 && button.getHeight() > 0) {
                buttons.add(button);
            }
            if (child instanceof Container nested) {
                buttons.addAll(findVisibleButtons(nested));
            }
        }
        return buttons;
    }

    private static void clickAt(Container screen, int x, int y) {
        Component target = SwingUtilities.getDeepestComponentAt(screen, x, y);
        if (target == null) {
            return;
        }
        Point point = SwingUtilities.convertPoint(screen, x, y, target);
        long when = System.currentTimeMillis();
        target.dispatchEvent(new MouseEvent(
                target, MouseEvent.MOUSE_PRESSED, when, InputEvent.BUTTON1_DOWN_MASK,
                point.x, point.y, 1, false, MouseEvent.BUTTON1));
        target.dispatchEvent(new MouseEvent(
                target, MouseEvent.MOUSE_RELEASED, when + 1, 0,
                point.x, point.y, 1, false, MouseEvent.BUTTON1));
    }

    private static String expectedNavigation(JButton button, PantallaBase screen) {
        return switch (button.getText()) {
            case "Menú principal", "Volver al menú" -> "menu";
            case "Comedor y alimentos" -> "comedor";
            case "Mesas disponibles" -> "mesas";
            case "Pago", "Ir a pago" -> "pago";
            case "Ticket", "Abrir pantalla de ticket" -> "ticket";
            case "Caja" -> "cajero";
            case "Estadísticas" -> "estadisticas";
            case "Iniciar sesión" -> screen instanceof Login ? null : "login";
            case "Salir" -> "salir";
            case "Abrir" -> ((MenuPrincipal) screen).getSelectorVentanas().getVentanaActual();
            default -> null;
        };
    }

    private static JButton findButton(Container container, String label) {
        for (java.awt.Component child : container.getComponents()) {
            if (child instanceof JButton button && label.equals(button.getText())) {
                return button;
            }
            if (child instanceof Container nested) {
                JButton result = findButtonOrNull(nested, label);
                if (result != null) {
                    return result;
                }
            }
        }
        throw new AssertionError("Button not found: " + label);
    }

    private static JButton findButtonOrNull(Container container, String label) {
        for (java.awt.Component child : container.getComponents()) {
            if (child instanceof JButton button && label.equals(button.getText())) {
                return button;
            }
            if (child instanceof Container nested) {
                JButton result = findButtonOrNull(nested, label);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
