package ms_soporte.ms_soporte.controller;

import ms_soporte.ms_soporte.exception.GlobalExceptionHandler;
import ms_soporte.ms_soporte.exception.RecursoNoEncontradoException;
import ms_soporte.ms_soporte.model.EstadoTicket;
import ms_soporte.ms_soporte.model.Ticket;
import ms_soporte.ms_soporte.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCrearTicket_Exito() throws Exception {
        Ticket ticketRespuesta = new Ticket(1L, 1L, "Ayuda", "Desc", EstadoTicket.ABIERTO, null);

        when(ticketService.crearTicket(any(Ticket.class))).thenReturn(ticketRespuesta);

        String jsonEnvio = """
                {
                    "idUsuario": 1,
                    "asunto": "Ayuda",
                    "descripcion": "Desc"
                }
                """;

        mockMvc.perform(post("/api/soporte/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEnvio))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.asunto").value("Ayuda"));
    }

    @Test
    void testCrearTicket_FallaValidacion() throws Exception {
        String jsonInvalido = """
                {
                    "idUsuario": null,
                    "asunto": "",
                    "descripcion": ""
                }
                """;

        mockMvc.perform(post("/api/soporte/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListarTodos() throws Exception {
        when(ticketService.obtenerTodos()).thenReturn(Arrays.asList(new Ticket()));

        mockMvc.perform(get("/api/soporte/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testObtenerPorId_Exito() throws Exception {
        Ticket ticket = new Ticket(1L, 1L, "A", "D", EstadoTicket.ABIERTO, null);
        when(ticketService.obtenerTicketPorId(1L)).thenReturn(ticket);

        mockMvc.perform(get("/api/soporte/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        when(ticketService.obtenerTicketPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("Ticket no encontrado"));

        mockMvc.perform(get("/api/soporte/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Ticket no encontrado"));
    }

    @Test
    void testObtenerPorUsuario() throws Exception {
        when(ticketService.obtenerTicketsPorUsuario(1L)).thenReturn(Arrays.asList(new Ticket()));

        mockMvc.perform(get("/api/soporte/tickets/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testActualizarEstado() throws Exception {
        Ticket ticketActualizado = new Ticket(1L, 1L, "A", "D", EstadoTicket.EN_PROGRESO, null);
        when(ticketService.actualizarEstado(eq(1L), any(EstadoTicket.class))).thenReturn(ticketActualizado);

        mockMvc.perform(patch("/api/soporte/tickets/1/estado")
                .param("estado", "EN_PROGRESO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROGRESO"));
    }
}