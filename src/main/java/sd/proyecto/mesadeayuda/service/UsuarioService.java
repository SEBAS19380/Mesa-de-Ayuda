package sd.proyecto.mesadeayuda.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import sd.proyecto.mesadeayuda.model.Usuario;
import sd.proyecto.mesadeayuda.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(
            String nombre,
            String correo,
            String password) {

        Usuario usuario = new Usuario(
                null,
                nombre,
                correo,
                password
        );

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public boolean validarLogin(String correo, String password) {

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);

        if (usuario.isPresent()) {
            return usuario.get().getPassword().equals(password);
        }

        return false;
    }

    public boolean existeCorreo(String correo) {

    return usuarioRepository.findByCorreo(correo).isPresent();
    
    }

    public Usuario obtenerPorCorreo(String correo) {

    return usuarioRepository
            .findByCorreo(correo)
            .orElseThrow(() ->
                    new RuntimeException("Usuario no encontrado"));
}

}