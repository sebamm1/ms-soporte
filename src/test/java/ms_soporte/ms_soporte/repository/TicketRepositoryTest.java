package ms_soporte.ms_soporte.repository;

import ms_soporte.ms_soporte.model.EstadoTicket;
import ms_soporte.ms_soporte.model.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketRepositoryTest {

    @Mock
    private TicketRepository ticketRepository;

    @Test
    void testFindByIdUsuario() {
        Ticket ticket = new Ticket(1L, 100L, "Asunto", "Desc", EstadoTicket.ABIERTO, null);
        when(ticketRepository.findByIdUsuario(100L)).thenReturn(List.of(ticket));

        List<Ticket> resultado = ticketRepository.findByIdUsuario(100L);

        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getIdUsuario());
    }
}