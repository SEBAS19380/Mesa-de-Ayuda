package sd.proyecto.mesadeayuda.controller;

import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("ticket", new Ticket(null, "", "", "", ""));

        return "tickets";
    }

    @GetMapping("/tickets/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute("ticket", new Ticket(null, "", "", "", ""));

        return "tickets";
    }

    @PostMapping("/tickets/agregar")
    public String agregarTicket(
            @Valid Ticket ticket,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("tickets", ticketService.listar());

            return "tickets";
        }

        ticketService.agregar(
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getPrioridad(),
                ticket.getResponsable());

        return "redirect:/tickets";
    }
}