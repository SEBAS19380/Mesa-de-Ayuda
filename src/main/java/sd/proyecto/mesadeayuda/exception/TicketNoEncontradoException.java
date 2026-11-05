package sd.proyecto.mesadeayuda.exception;

public class TicketNoEncontradoException
        extends RuntimeException {

    public TicketNoEncontradoException(
            Long id) {

        super(
                "No existe un ticket con el ID "
                + id);
    }
}