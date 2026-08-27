package sd.proyecto.mesadeayuda.controller;

import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketRestController {

    private final TicketService ticketService;

    public TicketRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "Proyecto Mesa de Ayuda funcionando";
    }

    @GetMapping("/tickets")
    public List<Ticket> tickets() {
        return ticketService.listar();
    }
}
