package com.proyectoingsw.cafeteria.ui.boundary;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.proyectoingsw.cafeteria.application.AutenticarUsuarioControlador;
import com.proyectoingsw.cafeteria.application.ComprarAlimentoManejador;
import com.proyectoingsw.cafeteria.application.ControladorTickets;
import com.proyectoingsw.cafeteria.application.FacturaControlador;
import com.proyectoingsw.cafeteria.application.PagarEfectivo;
import com.proyectoingsw.cafeteria.application.SeleccionarAlimentoControlador;
import com.proyectoingsw.cafeteria.domain.Usuario;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class PantallaRelationshipTest {
    @Test
    void screensOwnPrivateFormAndViewInstances() throws ReflectiveOperationException {
        SeleccionarAlimentoControlador foodControl =
                stub(SeleccionarAlimentoControlador.class);
        PantallaComedor comedor1 = new PantallaComedor(foodControl);
        PantallaComedor comedor2 = new PantallaComedor(foodControl);
        assertDistinctPrivateChildren(comedor1, comedor2, "menu");
        assertDistinctPrivateChildren(comedor1, comedor2, "busqueda");
        assertSame(foodControl, comedor1.getControlAlimentos());
        assertSame(foodControl, comedor2.getControlAlimentos());

        ComprarAlimentoManejador purchaseControl = stub(ComprarAlimentoManejador.class);
        PantallaPago pago1 = new PantallaPago(purchaseControl);
        PantallaPago pago2 = new PantallaPago(purchaseControl);
        assertDistinctPrivateChildren(pago1, pago2, "formulario");
        assertSame(purchaseControl, pago1.getControl());
        assertSame(purchaseControl, pago2.getControl());

        AutenticarUsuarioControlador authentication =
                stub(AutenticarUsuarioControlador.class);
        Usuario user = new Usuario("Ana", "secret");
        Login login1 = new Login(authentication, user);
        Login login2 = new Login(authentication, user);
        assertDistinctPrivateChildren(login1, login2, "formulario");
        assertSame(authentication, login1.getAutenticacion());
        assertSame(user, login1.getUsuario());

        ControladorTickets ticketControl = stub(ControladorTickets.class);
        PagarEfectivo cashPayment = stub(PagarEfectivo.class);
        FacturaControlador invoiceControl = stub(FacturaControlador.class);
        PantallaCajero cashier1 = new PantallaCajero(
                ticketControl, ticketControl, cashPayment, invoiceControl);
        PantallaCajero cashier2 = new PantallaCajero(
                ticketControl, ticketControl, cashPayment, invoiceControl);
        assertDistinctPrivateChildren(cashier1, cashier2, "vistaPedidos");
        assertSame(ticketControl, cashier1.getControladorTickets());
        assertSame(cashPayment, cashier1.getPagos());
        assertSame(invoiceControl, cashier1.getControlFacturas());
    }

    private static void assertDistinctPrivateChildren(
            Object firstScreen,
            Object secondScreen,
            String fieldName) throws ReflectiveOperationException {
        Field childField = firstScreen.getClass().getDeclaredField(fieldName);
        assertTrue(Modifier.isPrivate(childField.getModifiers()));
        assertTrue(Modifier.isFinal(childField.getModifiers()));
        childField.setAccessible(true);
        assertNotSame(childField.get(firstScreen), childField.get(secondScreen));
    }

    private static <T> T stub(Class<T> type) {
        return type.cast(java.lang.reflect.Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[] {type},
                (proxy, method, arguments) -> {
                    if (method.getDeclaringClass() == Object.class) {
                        return switch (method.getName()) {
                            case "toString" -> type.getSimpleName() + " test double";
                            case "hashCode" -> System.identityHashCode(proxy);
                            case "equals" -> proxy == arguments[0];
                            default -> throw new UnsupportedOperationException(method.getName());
                        };
                    }
                    Class<?> returnType = method.getReturnType();
                    if (returnType == boolean.class) {
                        return false;
                    }
                    if (returnType == int.class) {
                        return 0;
                    }
                    if (returnType == long.class) {
                        return 0L;
                    }
                    if (returnType == double.class) {
                        return 0.0;
                    }
                    if (returnType == float.class) {
                        return 0.0f;
                    }
                    if (returnType == short.class) {
                        return (short) 0;
                    }
                    if (returnType == byte.class) {
                        return (byte) 0;
                    }
                    if (returnType == char.class) {
                        return '\0';
                    }
                    return null;
                }));
    }
}
