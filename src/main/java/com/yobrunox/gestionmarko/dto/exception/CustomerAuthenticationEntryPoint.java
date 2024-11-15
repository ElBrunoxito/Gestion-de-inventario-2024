package com.yobrunox.gestionmarko.dto.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
/*        // Puedes devolver un mensaje personalizado
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{ \"error\": \"Acceso denegado. No tienes los permisos necesarios.\" }");
        response.getWriter().flush();
*/
        /*if (authException instanceof BusinessException) {
            BusinessException ex = (BusinessException) authException;
            response.setStatus(ex.getStatus().value());
            response.setContentType("application/json");

            String jsonResponse = String.format("{ \"code\": \"%s\", \"message\": \"%s\" }", ex.getCode(), ex.getMessage());
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        } else {
            // Manejamos otras excepciones de autenticación aquí si es necesario
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{ \"error\": \"Acceso denegado. No tienes los permisos necesarios.\" }");
            response.getWriter().flush();
        }*/
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Respuesta personalizada para errores de autenticación
        String jsonResponse = String.format("{ \"code\": \"%s\", \"message\": \"%s\" }", "M-403", "Acceso denegado. No tienes los permisos necesarios.");

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
