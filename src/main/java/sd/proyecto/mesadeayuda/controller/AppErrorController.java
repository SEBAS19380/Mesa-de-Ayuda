package sd.proyecto.mesadeayuda.controller;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AppErrorController implements ErrorController {

        @RequestMapping("/error")
        public String error(
                        HttpServletRequest request,
                        HttpSession session,
                        @CookieValue(name = "tema", defaultValue = "claro") String tema,
                        Model model) {

                Object estado = request.getAttribute(
                                RequestDispatcher.ERROR_STATUS_CODE);

                int codigo = estado == null
                                ? 500
                                : Integer.parseInt(estado.toString());

                String mensaje = codigo == 404
                                ? "La página solicitada no existe."
                                : "No pudimos completar la solicitud.";

                String temaPermitido = "oscuro".equals(tema)
                                ? "oscuro"
                                : "claro";

                boolean sesionActiva = session.getAttribute("usuarioLogueado") != null;

                model.addAttribute("codigo", codigo);
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("tema", temaPermitido);
                model.addAttribute("sesionActiva", sesionActiva);

                return "error";
        }
}