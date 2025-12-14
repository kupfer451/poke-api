package com.pokestore.poke_api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * Interceptor que verifica las anotaciones @AdminOnly y @Authenticated
 * en los métodos de los controllers y valida los permisos correspondientes.
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws IOException {

        // Solo procesar métodos de controllers
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Verificar si el método o la clase requieren @AdminOnly
        boolean requiresAdmin = handlerMethod.hasMethodAnnotation(AdminOnly.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);

        // Verificar si el método o la clase requieren @Authenticated
        boolean requiresAuth = handlerMethod.hasMethodAnnotation(Authenticated.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(Authenticated.class);

        // Si requiere admin, también requiere autenticación
        if (requiresAdmin) {
            requiresAuth = true;
        }

        // Obtener información del request (establecida por JwtAuthFilter)
        Boolean isAuthenticated = (Boolean) request.getAttribute("authenticated");
        Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");

        // Verificar autenticación
        if (requiresAuth && (isAuthenticated == null || !isAuthenticated)) {
            sendUnauthorizedResponse(response, "Token de autenticación requerido");
            return false;
        }

        // Verificar rol de admin
        if (requiresAdmin && (isAdmin == null || !isAdmin)) {
            sendForbiddenResponse(response, "Acceso denegado. Se requiere rol de administrador");
            return false;
        }

        return true;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"error\": \"Unauthorized\", \"message\": \"%s\", \"status\": 401}", message));
    }

    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"error\": \"Forbidden\", \"message\": \"%s\", \"status\": 403}", message));
    }
}
