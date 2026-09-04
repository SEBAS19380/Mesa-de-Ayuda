package sd.proyecto.mesadeayuda.controller;

import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.service.TicketService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public String listarTickets(Model model) {

        List<Ticket> tickets = ticketService.listar();

        model.addAttribute("tickets", tickets);

        return "tickets";
    }

    @PostMapping("/tickets/agregar")
    public String agregarTicket(
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String prioridad,
            @RequestParam String responsable,
            Model model) {
        ticketService.agregar(titulo, descripcion, prioridad, responsable);
        model.addAttribute("mensaje", "Ticket agregado correctamente.");
        model.addAttribute("tickets", ticketService.listar());
        return "tickets";
    }
}
