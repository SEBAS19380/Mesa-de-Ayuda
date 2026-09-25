package sd.proyecto.mesadeayuda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sd.proyecto.mesadeayuda.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}