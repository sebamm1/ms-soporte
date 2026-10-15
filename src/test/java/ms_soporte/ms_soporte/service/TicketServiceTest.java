package ms_soporte.ms_soporte.service;

import ms_soporte.ms_soporte.exception.RecursoNoEncontradoException;
import ms_soporte.ms_soporte.model.EstadoTicket;
import ms_soporte.ms_soporte.model.Ticket;
import ms_soporte.ms_soporte.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticketBase;

    @BeforeEach
    void setUp() {
        ticketBase = new Ticket(1L, 10L, "Error", "No funciona", EstadoTicket.ABIERTO, LocalDateTime.now());
    }

    @Test
    void testCrearTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketBase);
        Ticket creado = ticketService.crearTicket(new Ticket());
        assertNotNull(creado);
        assertEquals(1L, creado.getId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testObtenerTodos() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticketBase));
        List<Ticket> lista = ticketService.obtenerTodos();
        assertEquals(1, lista.size());
    }

    @Test
    void testObtenerTicketsPorUsuario() {
        when(ticketRepository.findByIdUsuario(10L)).thenReturn(Arrays.asList(ticketBase));
        List<Ticket> lista = ticketService.obtenerTicketsPorUsuario(10L);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getIdUsuario());
    }

    @Test
    void testObtenerTicketPorId_Exito() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketBase));
        Ticket encontrado = ticketService.obtenerTicketPorId(1L);
        assertEquals(1L, encontrado.getId());
    }

    @Test
    void testObtenerTicketPorId_Falla() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> ticketService.obtenerTicketPorId(99L));
    }

    @Test
    void testActualizarEstado() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketBase));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketBase);

        Ticket actualizado = ticketService.actualizarEstado(1L, EstadoTicket.RESUELTO);

        assertEquals(EstadoTicket.RESUELTO, actualizado.getEstado());
        verify(ticketRepository).save(ticketBase);
    }
}