package sd.proyecto.mesadeayuda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sd.proyecto.mesadeayuda.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByTituloContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrResponsableContainingIgnoreCase(
            String titulo,
            String descripcion,
            String responsable,
            Pageable pageable);

}