package sd.proyecto.mesadeayuda.service;

import sd.proyecto.mesadeayuda.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    public List<Ticket> listar() {

        return List.of(
                new Ticket(
                        1L,
                        "Computador no enciende",
                        "El equipo no enciende al presionar el botón de inicio.",
                        "ALTA",
                        "Carlos Pérez"
                ),

                new Ticket(
                        2L,
                        "Problema con impresora",
                        "La impresora no reconoce los documentos enviados.",
                        "MEDIA",
                        "Ana Gómez"
                ),

                new Ticket(
                        3L,
                        "Error en sistema",
                        "El sistema presenta un error al iniciar sesión.",
                        "ALTA",
                        "Luis Rodríguez"
                ),

                new Ticket(
                        4L,
                        "Solicitud de instalación",
                        "Se solicita instalar un programa en el equipo.",
                        "BAJA",
                        "María López"
                )
        );
    }
}
