package sd.proyecto.mesadeayuda.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import sd.proyecto.mesadeayuda.DTO.UsuarioDTO;
import sd.proyecto.mesadeayuda.service.UsuarioService;

@Controller
public class InicioController {

    private final UsuarioService usuarioService;

    public InicioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String inicio() {

        return "index";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(
            Model model) {

        model.addAttribute(
                "usuario",
                new UsuarioDTO());

        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @Valid
            @ModelAttribute("usuario")
            UsuarioDTO usuarioDTO,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "registro";
        }

        if (!usuarioDTO.getPassword()
                .equals(usuarioDTO.getConfirmarPassword())) {

            model.addAttribute(
                    "errorPassword",
                    "Las contraseñas no coinciden");

            return "registro";
        }

        if (usuarioService.existeCorreo(
                usuarioDTO.getCorreo())) {

            model.addAttribute(
                    "errorCorreo",
                    "El correo ya está registrado");

            return "registro";
        }

        usuarioService.registrar(
                usuarioDTO.getNombre(),
                usuarioDTO.getCorreo(),
                usuarioDTO.getPassword());

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin(
            Model model,
            HttpSession session) {

        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/menu";
        }

        model.addAttribute(
                "usuario",
                new UsuarioDTO());

        return "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(
            @ModelAttribute("usuario")
            UsuarioDTO usuarioDTO,
            Model model,
            HttpSession session) {

        boolean loginValido =
                usuarioService.validarLogin(
                        usuarioDTO.getCorreo(),
                        usuarioDTO.getPassword());

        if (!loginValido) {

            model.addAttribute(
                    "error",
                    "Correo o contraseña incorrectos");

            return "login";
        }

        session.setAttribute(
                "usuarioLogueado",
                usuarioService.obtenerPorCorreo(
                        usuarioDTO.getCorreo()));

        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String mostrarMenu(
            @CookieValue(
                    name = "tema",
                    defaultValue = "claro")
            String tema,
            Model model) {

        String temaPermitido =
                "oscuro".equals(tema)
                        ? "oscuro"
                        : "claro";

        model.addAttribute(
                "tema",
                temaPermitido);

        return "menu";
    }

    @PostMapping("/logout")
    public String cerrarSesion(
            HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}