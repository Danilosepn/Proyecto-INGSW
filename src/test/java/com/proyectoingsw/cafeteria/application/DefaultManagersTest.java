package com.proyectoingsw.cafeteria.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.proyectoingsw.cafeteria.domain.PreguntaFAQ;
import com.proyectoingsw.cafeteria.domain.Resenia;
import com.proyectoingsw.cafeteria.persistence.InMemoryPreguntaFAQDAO;
import com.proyectoingsw.cafeteria.persistence.InMemoryReseniaDAO;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultManagersTest {
    @Test
    void faqManagerUsesItsDaoWithoutOwningIt() {
        InMemoryPreguntaFAQDAO dao = new InMemoryPreguntaFAQDAO(pregunta -> "manana");
        DefaultAdministradorFAQ manager = new DefaultAdministradorFAQ(dao);
        PreguntaFAQ faq = new PreguntaFAQ(
                "faq-1", LocalDate.of(2026, 11, 11), "comida", "¿Qué hay?", "Menú");

        manager.proponerFAQ(faq);

        assertEquals(List.of(faq), manager.obtenerFAQs());
        assertEquals(List.of(faq), dao.buscarPorFranjaHoraria("manana"));
    }

    @Test
    void reviewManagerPublishesAndListsViaItsDao() {
        InMemoryReseniaDAO dao = new InMemoryReseniaDAO(resenia -> "est-1");
        DefaultGestorResenia manager = new DefaultGestorResenia(dao);
        Resenia review = new Resenia(
                "review-1", "Ana", LocalDate.of(2026, 11, 11), "servicio", false);

        manager.publicarResenia(review);

        assertEquals(List.of(review), manager.obtenerResenias());
        assertEquals(List.of(review), dao.buscarPorEstudiante("est-1"));
    }
}
