package com.pokestore.poke_api.security;

import com.pokestore.poke_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que extrae la información del JWT y la coloca en los atributos del request
 * para que los interceptores y controllers puedan acceder a ella.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtService.isTokenValid(token)) {
                    String userId = jwtService.getUserIdFromToken(token);
                    String email = jwtService.getEmailFromToken(token);
                    Boolean isAdmin = jwtService.getIsAdminFromToken(token);

                    // Guardar información en los atributos del request
                    request.setAttribute("userId", userId);
                    request.setAttribute("email", email);
                    request.setAttribute("isAdmin", isAdmin != null && isAdmin);
                    request.setAttribute("authenticated", true);
                }
            } catch (Exception e) {
                // Token inválido, continuar sin autenticación
                request.setAttribute("authenticated", false);
            }
        } else {
            request.setAttribute("authenticated", false);
        }

        filterChain.doFilter(request, response);
    }
}
