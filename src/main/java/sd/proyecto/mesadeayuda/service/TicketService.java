package sd.proyecto.mesadeayuda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import sd.proyecto.mesadeayuda.model.Ticket;
import sd.proyecto.mesadeayuda.repository.TicketRepository;
import sd.proyecto.mesadeayuda.model.Usuario;

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
            Integer prioridad,
            String responsable,
            Usuario usuario) {

        Ticket nuevoTicket = new Ticket(
                null,
                titulo,
                descripcion,
                prioridad,
                responsable);

                nuevoTicket.setUsuario(usuario);

        return ticketRepository.save(nuevoTicket);
    }

    public Optional<Ticket> buscarPorId(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket actualizar(
            Long id,
            String titulo,
            String descripcion,
            Integer prioridad,
            String responsable) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        ticket.setTitulo(titulo);
        ticket.setDescripcion(descripcion);
        ticket.setPrioridad(prioridad);
        ticket.setResponsable(responsable);

        return ticketRepository.save(ticket);
    }

    public Page<Ticket> listarPaginado(
            int pagina,
            int tamano) {

        Pageable pageable = PageRequest.of(
                pagina,
                tamano);

        return ticketRepository.findAll(
                pageable);
    }

    public Page<Ticket> buscar(
            String criterio,
            int pagina,
            int tamano) {

        Pageable pageable = PageRequest.of(
                pagina,
                tamano);

        return ticketRepository
                .findByTituloContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrResponsableContainingIgnoreCase(
                        criterio,
                        criterio,
                        criterio,
                        pageable);
    }

    public void eliminar(Long id) {
        ticketRepository.deleteById(id);
    }
}