package sd.proyecto.mesadeayuda.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
            throws IOException {

        HttpSession session = request.getSession(false);

        boolean sesionActiva = session != null
                && session.getAttribute("usuarioLogueado") != null;

        if (!sesionActiva) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return false;
        }

        return true;
    }
}