package sd.proyecto.mesadeayuda.runner;

import org.reactivestreams.Subscription;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import sd.proyecto.mesadeayuda.reactive.TicketReactivo;
import sd.proyecto.mesadeayuda.service.TicketReactivoService;

@Component
@ConditionalOnProperty(
        name = "demo.reactiva.enabled",
        havingValue = "true")
public class TicketReactivoRunner
        implements CommandLineRunner {

    private final TicketReactivoService ticketReactivoService;

    public TicketReactivoRunner(
            TicketReactivoService ticketReactivoService) {

        this.ticketReactivoService =
                ticketReactivoService;
    }

    @Override
    public void run(
            String... args) {

        imprimirEncabezadoGeneral();

        demostrarTicketsActivos();
        demostrarConteoConMono();
        demostrarCasoSinActivos();
        demostrarOnError();
        demostrarControlDeDemanda();

        imprimirSeparador();
        System.out.println(
                "DEMOSTRACIÓN REACTIVA FINALIZADA");
        imprimirSeparador();
    }

    private void demostrarTicketsActivos() {

        imprimirTitulo(
                "1. PROCESAMIENTO DE TICKETS ACTIVOS");

        Flux<String> flujoProcesado =
                ticketReactivoService
                        .procesarTicketsActivos();

        System.out.println(
                "El flujo fue definido.");

        System.out.println(
                "Todavía no se han consumido elementos.");

        System.out.println(
                "Iniciando suscripción...");

        flujoProcesado.subscribe(
                dato -> System.out.println(
                        "onNext: " + dato),

                error -> System.out.println(
                        "onError: "
                        + error.getMessage()),

                () -> System.out.println(
                        "onComplete: procesamiento "
                        + "finalizado correctamente"));
    }

    private void demostrarConteoConMono() {

        imprimirTitulo(
                "2. CONTEO DE TICKETS ACTIVOS CON MONO");

        ticketReactivoService
                .contarTicketsActivos()
                .subscribe(
                        total -> System.out.println(
                                "onNext: total de tickets "
                                + "activos = "
                                + total),

                        error -> System.out.println(
                                "onError: "
                                + error.getMessage()),

                        () -> System.out.println(
                                "onComplete: conteo "
                                + "finalizado correctamente"));
    }

    private void demostrarCasoSinActivos() {

        imprimirTitulo(
                "3. CASO SIN TICKETS ACTIVOS");

        System.out.println(
                "Procesando una fuente con cuatro "
                + "tickets inactivos...");

        ticketReactivoService
                .procesarCasoSinActivos()
                .subscribe(
                        dato -> System.out.println(
                                "onNext: " + dato),

                        error -> System.out.println(
                                "onError: "
                                + error.getMessage()),

                        () -> System.out.println(
                                "onComplete: no se encontraron "
                                + "tickets activos"));

        ticketReactivoService
                .contarCasoSinActivos()
                .subscribe(
                        total -> System.out.println(
                                "Conteo con Mono: "
                                + total
                                + " tickets activos"),

                        error -> System.out.println(
                                "onError durante el conteo: "
                                + error.getMessage()),

                        () -> System.out.println(
                                "onComplete: conteo del caso "
                                + "sin activos finalizado"));
    }

    private void demostrarOnError() {

        imprimirTitulo(
                "4. DEMOSTRACIÓN DE onError");

        ticketReactivoService
                .procesarConErrorSimulado()
                .subscribe(
                        dato -> System.out.println(
                                "onNext: " + dato),

                        error -> System.out.println(
                                "onError: "
                                + error.getMessage()),

                        () -> System.out.println(
                                "onComplete: este mensaje "
                                + "no debe aparecer"));
    }

    private void demostrarControlDeDemanda() {

        imprimirTitulo(
                "5. CONTROL DE DEMANDA Y CANCELACIÓN");

        Flux<TicketReactivo> fuente =
                ticketReactivoService
                        .obtenerTickets()
                        .doOnRequest(cantidad ->
                                System.out.println(
                                        "Demanda solicitada: "
                                        + cantidad));

        fuente.subscribe(
                new BaseSubscriber<TicketReactivo>() {

                    private int cantidadRecibida;

                    @Override
                    protected void hookOnSubscribe(
                            Subscription subscription) {

                        System.out.println(
                                "Suscripción recibida.");

                        System.out.println(
                                "Solicitando 1 ticket...");

                        request(1);
                    }

                    @Override
                    protected void hookOnNext(
                            TicketReactivo ticket) {

                        cantidadRecibida++;

                        System.out.println(
                                "Recibido individualmente: "
                                + "Ticket "
                                + ticket.id()
                                + " - "
                                + ticket.titulo());

                        if (cantidadRecibida == 3) {

                            System.out.println(
                                    "Se recibieron 3 tickets.");

                            System.out.println(
                                    "Cancelando la suscripción...");

                            cancel();

                            return;
                        }

                        System.out.println(
                                "Solicitando 1 ticket más...");

                        request(1);
                    }

                    @Override
                    protected void hookOnComplete() {

                        System.out.println(
                                "onComplete: todos los tickets "
                                + "fueron procesados");
                    }

                    @Override
                    protected void hookOnError(
                            Throwable throwable) {

                        System.out.println(
                                "onError: "
                                + throwable.getMessage());
                    }

                    @Override
                    protected void hookOnCancel() {

                        System.out.println(
                                "Suscripción cancelada.");

                        System.out.println(
                                "No se recibirán más tickets.");
                    }

                    @Override
                    protected void hookFinally(
                            SignalType tipoSenal) {

                        System.out.println(
                                "Señal final observada: "
                                + tipoSenal);
                    }
                });
    }

    private void imprimirEncabezadoGeneral() {

        System.out.println();
        imprimirSeparador();

        System.out.println(
                "DEMOSTRACIÓN REACTIVA - MESA DE AYUDA");

        System.out.println(
                "Flux, Mono, operadores, señales "
                + "y control de demanda");

        imprimirSeparador();
    }

    private void imprimirTitulo(
            String titulo) {

        System.out.println();
        System.out.println(
                "=== " + titulo + " ===");
    }

    private void imprimirSeparador() {

        System.out.println(
                "==================================================");
    }
}