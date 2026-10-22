package sd.proyecto.mesadeayuda.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PreferenciaController {

    @PostMapping("/preferencia/tema")
    public String guardarTema(
            @RequestParam String tema,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        String temaSeleccionado;

        if ("oscuro".equals(tema)) {
            temaSeleccionado = "oscuro";
        } else {
            temaSeleccionado = "claro";
        }

        Cookie cookieTema = new Cookie(
                "tema",
                temaSeleccionado);

        cookieTema.setMaxAge(
                7 * 24 * 60 * 60);

        cookieTema.setPath("/");
        cookieTema.setHttpOnly(true);

        response.addCookie(cookieTema);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Preferencia visual guardada correctamente.");

        return "redirect:/menu";
    }
}