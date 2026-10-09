package sd.proyecto.mesadeayuda.controller;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import sd.proyecto.mesadeayuda.DTO.TicketDTO;
import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.model.Usuario;
import sd.proyecto.mesadeayuda.service.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listarTickets(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(required = false) String buscar,
            Model model,
            HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }

        Page<Ticket> page = ticketService.listarPaginado(
                pagina,
                5);

        model.addAttribute(
                "tickets",
                page.getContent());

        model.addAttribute(
                "paginaActual",
                pagina);

        model.addAttribute(
                "totalPaginas",
                page.getTotalPages());

        model.addAttribute(
                "buscar",
                buscar);

        if (buscar != null && !buscar.isBlank()) {

            model.addAttribute(
                    "resultadosBusqueda",
                    ticketService.buscar(
                            buscar,
                            0,
                            9999)
                            .getContent());

        } else {

            model.addAttribute(
                    "resultadosBusqueda",
                    Collections.emptyList());
        }

        model.addAttribute(
                "ticket",
                new TicketDTO());

        return "tickets";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(
            Model model,
            HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }

        cargarDatosPrincipales(model);

        model.addAttribute(
                "ticket",
                new TicketDTO());

        return "tickets";
    }

    @PostMapping("/agregar")
    public String agregarTicket(
            @Valid @ModelAttribute("ticket") TicketDTO ticketDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {

            cargarDatosPrincipales(model);

            return "tickets";
        }

        Usuario usuario = (Usuario) session.getAttribute(
                "usuarioLogueado");

        ticketService.agregar(
                ticketDTO.getTitulo(),
                ticketDTO.getDescripcion(),
                ticketDTO.getPrioridad(),
                ticketDTO.getResponsable(),
                usuario);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Ticket agregado correctamente.");

        return "redirect:/tickets";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }

        Ticket ticket = ticketService.buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket no encontrado"));

        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setId(ticket.getId());
        ticketDTO.setTitulo(ticket.getTitulo());
        ticketDTO.setDescripcion(ticket.getDescripcion());
        ticketDTO.setPrioridad(ticket.getPrioridad());
        ticketDTO.setResponsable(ticket.getResponsable());

        cargarDatosPrincipales(model);

        model.addAttribute(
                "ticket",
                ticketDTO);

        model.addAttribute(
                "editar",
                true);

        return "tickets";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarTicket(
            @PathVariable Long id,
            @Valid @ModelAttribute("ticket") TicketDTO ticketDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {

            ticketDTO.setId(id);

            cargarDatosPrincipales(model);

            model.addAttribute(
                    "ticket",
                    ticketDTO);

            model.addAttribute(
                    "editar",
                    true);

            return "tickets";
        }

        ticketService.actualizar(
                id,
                ticketDTO.getTitulo(),
                ticketDTO.getDescripcion(),
                ticketDTO.getPrioridad(),
                ticketDTO.getResponsable());

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Ticket actualizado correctamente.");

        return "redirect:/tickets";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarTicket(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }

        ticketService.eliminar(id);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Ticket eliminado correctamente.");

        return "redirect:/tickets";
    }

    private void cargarDatosPrincipales(Model model) {

        Page<Ticket> page = ticketService.listarPaginado(
                0,
                5);

        model.addAttribute(
                "tickets",
                page.getContent());

        model.addAttribute(
                "paginaActual",
                0);

        model.addAttribute(
                "totalPaginas",
                page.getTotalPages());

        model.addAttribute(
                "buscar",
                "");

        model.addAttribute(
                "resultadosBusqueda",
                Collections.emptyList());
    }
}