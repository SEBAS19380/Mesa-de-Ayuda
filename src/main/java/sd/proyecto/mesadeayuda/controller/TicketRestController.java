package sd.proyecto.mesadeayuda.controller;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import sd.proyecto.mesadeayuda.DTO.TicketApiRequestDTO;
import sd.proyecto.mesadeayuda.DTO.TicketApiResponseDTO;
import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.model.Usuario;
import sd.proyecto.mesadeayuda.service.TicketService;
import sd.proyecto.mesadeayuda.service.UsuarioService;

@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

        private final TicketService ticketService;
        private final UsuarioService usuarioService;

        public TicketRestController(
                        TicketService ticketService,
                        UsuarioService usuarioService) {

                this.ticketService = ticketService;
                this.usuarioService = usuarioService;
        }

        @GetMapping
        public ResponseEntity<List<TicketApiResponseDTO>> consultarTodos() {

                List<TicketApiResponseDTO> tickets = ticketService.listar()
                                .stream()
                                .map(this::convertirARespuesta)
                                .toList();

                return ResponseEntity.ok(tickets);
        }

        @GetMapping("/{id}")
        public ResponseEntity<TicketApiResponseDTO> consultarPorId(
                        @PathVariable Long id) {

                Ticket ticket = ticketService.obtenerTicketPorId(id);

                TicketApiResponseDTO respuesta = convertirARespuesta(ticket);

                return ResponseEntity.ok(respuesta);
        }

        @PostMapping
        public ResponseEntity<TicketApiResponseDTO> crear(
                        @Valid @RequestBody TicketApiRequestDTO solicitud) {

                Usuario usuario = usuarioService.obtenerPorId(
                                solicitud.getUsuarioId());

                Ticket ticketCreado = ticketService.agregar(
                                solicitud.getTitulo(),
                                solicitud.getDescripcion(),
                                solicitud.getPrioridad(),
                                solicitud.getResponsable(),
                                usuario);

                TicketApiResponseDTO respuesta = convertirARespuesta(ticketCreado);

                URI ubicacion = URI.create(
                                "/api/tickets/"
                                                + ticketCreado.getId());

                return ResponseEntity
                                .created(ubicacion)
                                .body(respuesta);
        }

        @PutMapping("/{id}")
        public ResponseEntity<TicketApiResponseDTO> actualizar(
                        @PathVariable Long id,
                        @Valid @RequestBody TicketApiRequestDTO solicitud) {

                Usuario usuario = usuarioService.obtenerPorId(
                                solicitud.getUsuarioId());

                Ticket ticketActualizado = ticketService.actualizar(
                                id,
                                solicitud.getTitulo(),
                                solicitud.getDescripcion(),
                                solicitud.getPrioridad(),
                                solicitud.getResponsable(),
                                usuario);

                TicketApiResponseDTO respuesta = convertirARespuesta(
                                ticketActualizado);

                return ResponseEntity.ok(respuesta);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Map<String, Object>> eliminar(
                        @PathVariable Long id) {

                ticketService.eliminar(id);

                Map<String, Object> respuesta = new LinkedHashMap<>();

                respuesta.put(
                                "mensaje",
                                "Ticket eliminado correctamente");

                respuesta.put(
                                "id",
                                id);

                return ResponseEntity.ok(respuesta);
        }

        private TicketApiResponseDTO convertirARespuesta(
                        Ticket ticket) {

                Usuario usuario = ticket.getUsuario();

                Long usuarioId = usuario != null
                                ? usuario.getId()
                                : null;

                String usuarioNombre = usuario != null
                                ? usuario.getNombre()
                                : null;

                return new TicketApiResponseDTO(
                                ticket.getId(),
                                ticket.getTitulo(),
                                ticket.getDescripcion(),
                                ticket.getPrioridad(),
                                ticket.getResponsable(),
                                usuarioId,
                                usuarioNombre);
        }
}