package sd.proyecto.mesadeayuda.exception;

public class UsuarioNoEncontradoException
        extends RuntimeException {

    public UsuarioNoEncontradoException(
            Long id) {

        super(
                "No existe un usuario con el ID "
                + id);
    }
}