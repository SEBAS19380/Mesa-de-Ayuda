package sd.proyecto.mesadeayuda.service;

import sd.proyecto.mesadeayuda.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final List<Ticket> tickets = new ArrayList<>();

    public TicketService() {
        tickets.add(
                new Ticket(
                        1L,
                        "Computador no enciende",
                        "El equipo no enciende al presionar el botón de inicio.",
                        "ALTA",
                        "Carlos Pérez"
                )
        );

        tickets.add(
                new Ticket(
                        2L,
                        "Problema con impresora",
                        "La impresora no reconoce los documentos enviados.",
                        "MEDIA",
                        "Ana Gómez"
                )
        );

        tickets.add(
                new Ticket(
                        3L,
                        "Error en sistema",
                        "El sistema presenta un error al iniciar sesión.",
                        "ALTA",
                        "Luis Rodríguez"
                )
        );

        tickets.add(
                new Ticket(
                        4L,
                        "Solicitud de instalación",
                        "Se solicita instalar un programa en el equipo.",
                        "BAJA",
                        "María López"
                )
        );
    }

    public List<Ticket> listar() {
        return tickets;
    }

    public Ticket agregar(
            String titulo,
            String descripcion,
            String prioridad,
            String responsable) {

        Long nuevoId = generarNuevoId();

        Ticket nuevoTicket = new Ticket(
                nuevoId,
                titulo,
                descripcion,
                prioridad,
                responsable
        );

        tickets.add(nuevoTicket);

        return nuevoTicket;
    }

    private Long generarNuevoId() {
        return tickets.stream()
                .mapToLong(Ticket::getId)
                .max()
                .orElse(0L) + 1;
    }
}
