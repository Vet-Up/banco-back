package es.VetUp.banco_back.filter;

import es.VetUp.banco_back.b_domain.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Permitir preflight CORS y auth endpoints
        if ("OPTIONS".equalsIgnoreCase(method) || path.startsWith("/api/auth")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);

        UserDto user;
        try {
            jwtService.validateToken(token);
            user = jwtService.getUserFromToken(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Datos básicos disponibles para los controllers
        request.setAttribute("userId", user.userId());
        request.setAttribute("user", user);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
