package sd.proyecto.mesadeayuda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sd.proyecto.mesadeayuda.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}