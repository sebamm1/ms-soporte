package ms_soporte.ms_soporte.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void testConstructoresYGettersSetters() {
        LocalDateTime ahora = LocalDateTime.now();

        // Prueba constructor con parámetros
        Ticket ticket = new Ticket(1L, 10L, "Asunto", "Desc", EstadoTicket.ABIERTO, ahora);

        assertEquals(1L, ticket.getId());
        assertEquals(10L, ticket.getIdUsuario());
        assertEquals("Asunto", ticket.getAsunto());
        assertEquals("Desc", ticket.getDescripcion());
        assertEquals(EstadoTicket.ABIERTO, ticket.getEstado());
        assertEquals(ahora, ticket.getFechaCreacion());

        // Prueba constructor vacío y setters
        Ticket ticketVacio = new Ticket();
        ticketVacio.setId(2L);
        ticketVacio.setIdUsuario(20L);
        ticketVacio.setAsunto("Nuevo");
        ticketVacio.setDescripcion("Detalle");
        ticketVacio.setEstado(EstadoTicket.CERRADO);
        ticketVacio.setFechaCreacion(ahora);

        assertEquals(2L, ticketVacio.getId());
        assertEquals(20L, ticketVacio.getIdUsuario());
        assertEquals("Nuevo", ticketVacio.getAsunto());
        assertEquals("Detalle", ticketVacio.getDescripcion());
        assertEquals(EstadoTicket.CERRADO, ticketVacio.getEstado());
        assertEquals(ahora, ticketVacio.getFechaCreacion());
    }

    @Test
    void testOnCreateAsignaValoresPorDefecto() {
        Ticket ticket = new Ticket();
        ticket.onCreate();

        assertNotNull(ticket.getFechaCreacion());
        assertEquals(EstadoTicket.ABIERTO, ticket.getEstado());
    }

    @Test
    void testOnCreateNoSobrescribeEstadoSiYaExiste() {
        Ticket ticket = new Ticket();
        ticket.setEstado(EstadoTicket.EN_PROGRESO);
        ticket.onCreate();

        assertEquals(EstadoTicket.EN_PROGRESO, ticket.getEstado());
    }
}