package sd.proyecto.mesadeayuda.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> listar() {
        return ticketRepository.findAll();
    }

    public Ticket agregar(
            String titulo,
            String descripcion,
            String prioridad,
            String responsable) {

        Ticket nuevoTicket = new Ticket(
                null,
                titulo,
                descripcion,
                prioridad,
                responsable
        );

        return ticketRepository.save(nuevoTicket);
    }
}