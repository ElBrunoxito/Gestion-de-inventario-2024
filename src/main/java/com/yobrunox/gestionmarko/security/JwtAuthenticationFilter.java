package com.yobrunox.gestionmarko.security;

import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtService;
    private final UserDetailsService userDetailsService;
    //private final


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = getTokenFromRequest(request);
        String username = null;

        try {
            if (token != null) {
                username = jwtService.getUsernameFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        //throw new BusinessException("M-403", HttpStatus.BAD_REQUEST, "Token inválido");
                        //throw new
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            //throw new BusinessException("4002", HttpStatus.UNAUTHORIZED, "Token ha expirado");
            //throw new BusinessException("M-403", HttpStatus.BAD_REQUEST, "Acwwwwwceso denegado");
            throw new AuthenticationException("Acceso denegado."){};

        } catch (JwtException ex) {
            //andleBusinessException(response, new BusinessException("M-403", HttpStatus.BAD_REQUEST, "Acceso denegado"));
            throw new AuthenticationException("Acceso denegado. Token invalido o expirado"){};
        }
        //filterChain.doFilter(request,response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
        {
            return authHeader.substring(7);
        }
        return null;
    }
    private void handleBusinessException(HttpServletResponse response, BusinessException ex) throws IOException {
        response.setStatus(ex.getStatus().value());
        response.setContentType("application/json");

        String jsonResponse = String.format("{ \"code\": \"%s\", \"message\": \"%s\" }", ex.getCode(), ex.getMessage());

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}