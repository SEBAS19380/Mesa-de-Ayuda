package sd.proyecto.mesadeayuda.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(
        annotations = RestController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
            manejarErroresValidacion(
                    MethodArgumentNotValidException exception) {

        Map<String, String> errores =
                new LinkedHashMap<>();

        for (FieldError error
                : exception.getBindingResult()
                        .getFieldErrors()) {

            errores.putIfAbsent(
                    error.getField(),
                    error.getDefaultMessage());
        }

        Map<String, Object> respuesta =
                crearRespuestaBase(
                        HttpStatus.BAD_REQUEST,
                        "La solicitud contiene datos inválidos");

        respuesta.put(
                "errores",
                errores);

        return ResponseEntity
                .badRequest()
                .body(respuesta);
    }

    @ExceptionHandler(
            HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>>
            manejarJsonInvalido(
                    HttpMessageNotReadableException exception) {

        Map<String, Object> respuesta =
                crearRespuestaBase(
                        HttpStatus.BAD_REQUEST,
                        "El cuerpo de la solicitud contiene un JSON inválido");

        return ResponseEntity
                .badRequest()
                .body(respuesta);
    }

    @ExceptionHandler(
            MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>>
            manejarTipoDeParametroInvalido(
                    MethodArgumentTypeMismatchException exception) {

        String mensaje =
                "El parámetro '"
                + exception.getName()
                + "' debe tener un valor válido";

        Map<String, Object> respuesta =
                crearRespuestaBase(
                        HttpStatus.BAD_REQUEST,
                        mensaje);

        return ResponseEntity
                .badRequest()
                .body(respuesta);
    }

    @ExceptionHandler(
            TicketNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>>
            manejarTicketNoEncontrado(
                    TicketNoEncontradoException exception) {

        Map<String, Object> respuesta =
                crearRespuestaBase(
                        HttpStatus.NOT_FOUND,
                        exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler(
            UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>>
            manejarUsuarioNoEncontrado(
                    UsuarioNoEncontradoException exception) {

        Map<String, Object> respuesta =
                crearRespuestaBase(
                        HttpStatus.NOT_FOUND,
                        exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
            manejarErrorInterno(
                    Exception exception) {

        Map<String, Object> respuesta =
                crearRespuestaBase(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ocurrió un error interno al procesar la solicitud");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(respuesta);
    }

    private Map<String, Object> crearRespuestaBase(
            HttpStatus estado,
            String mensaje) {

        Map<String, Object> respuesta =
                new LinkedHashMap<>();

        respuesta.put(
                "fecha",
                LocalDateTime.now());

        respuesta.put(
                "codigo",
                estado.value());

        respuesta.put(
                "estado",
                estado.getReasonPhrase());

        respuesta.put(
                "mensaje",
                mensaje);

        return respuesta;
    }
}