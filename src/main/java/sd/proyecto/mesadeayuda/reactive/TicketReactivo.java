package sd.proyecto.mesadeayuda.reactive;

public record TicketReactivo(
        Long id,
        String titulo,
        Integer prioridad,
        String responsable,
        boolean activo) {
}