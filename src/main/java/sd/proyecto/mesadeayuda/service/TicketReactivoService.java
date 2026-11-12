package sd.proyecto.mesadeayuda.service;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import sd.proyecto.mesadeayuda.reactive.TicketReactivo;

@Service
public class TicketReactivoService {

    public Flux<TicketReactivo> obtenerTickets() {

        return Flux.just(
                new TicketReactivo(
                        101L,
                        "Error de acceso al sistema",
                        3,
                        "Soporte técnico",
                        true),
                new TicketReactivo(
                        102L,
                        "Impresora sin conexión",
                        2,
                        "Infraestructura",
                        true),
                new TicketReactivo(
                        103L,
                        "Solicitud cancelada",
                        1,
                        "Mesa de ayuda",
                        false),
                new TicketReactivo(
                        104L,
                        "Falla de conexión de red",
                        3,
                        "Redes",
                        true));
    }

    public Flux<String> procesarTicketsActivos() {

        return obtenerTickets()
                .filter(TicketReactivo::activo)
                .map(ticket ->
                        "Ticket "
                        + ticket.id()
                        + " | "
                        + ticket.titulo()
                        + " | Prioridad: "
                        + convertirPrioridad(
                                ticket.prioridad())
                        + " | Responsable: "
                        + ticket.responsable());
    }

    public Mono<Long> contarTicketsActivos() {

        return obtenerTickets()
                .filter(TicketReactivo::activo)
                .count();
    }

    public Flux<TicketReactivo>
            obtenerTicketsTodosInactivos() {

        return Flux.just(
                new TicketReactivo(
                        201L,
                        "Solicitud cerrada",
                        1,
                        "Mesa de ayuda",
                        false),
                new TicketReactivo(
                        202L,
                        "Equipo retirado",
                        2,
                        "Infraestructura",
                        false),
                new TicketReactivo(
                        203L,
                        "Acceso cancelado",
                        3,
                        "Seguridad informática",
                        false),
                new TicketReactivo(
                        204L,
                        "Solicitud duplicada",
                        1,
                        "Soporte técnico",
                        false));
    }

    public Flux<String>
            procesarCasoSinActivos() {

        return obtenerTicketsTodosInactivos()
                .filter(TicketReactivo::activo)
                .map(ticket ->
                        "Ticket activo: "
                        + ticket.id()
                        + " - "
                        + ticket.titulo());
    }

    public Mono<Long>
            contarCasoSinActivos() {

        return obtenerTicketsTodosInactivos()
                .filter(TicketReactivo::activo)
                .count();
    }

    public Flux<String>
            procesarConErrorSimulado() {

        return obtenerTickets()
                .filter(TicketReactivo::activo)
                .map(ticket -> {

                    if (ticket.id().equals(102L)) {

                        throw new IllegalStateException(
                                "Error simulado al procesar "
                                + "el ticket 102");
                    }

                    return "Ticket "
                            + ticket.id()
                            + " procesado correctamente";
                });
    }

    private String convertirPrioridad(
            Integer prioridad) {

        if (prioridad == null) {
            return "SIN PRIORIDAD";
        }

        return switch (prioridad) {
            case 3 -> "ALTA";
            case 2 -> "MEDIA";
            case 1 -> "BAJA";
            default -> "DESCONOCIDA";
        };
    }
}