package sd.proyecto.mesadeayuda.controller;

import sd.proyecto.mesadeayuda.DTO.TicketDTO;
import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
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
        model.addAttribute("ticket", new TicketDTO());

        return "tickets";
    }

    @GetMapping("/tickets/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute("ticket", new TicketDTO());

        return "tickets";
    }

    @PostMapping("/tickets/agregar")
    public String agregarTicket(
            @Valid @ModelAttribute("ticket") TicketDTO ticketDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("tickets", ticketService.listar());

            return "tickets";
        }

        ticketService.agregar(
                ticketDTO.getTitulo(),
                ticketDTO.getDescripcion(),
                ticketDTO.getPrioridad(),
                ticketDTO.getResponsable());

        return "redirect:/tickets";
    }
}