package es.VetUp.banco_back.filter;

import es.VetUp.banco_back.b_domain.service.JwtService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtFilter jwtFilter;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter(jwtService);
        testUser = new UserDto(
                1L,
                "testuser",
                "password123",
                "John",
                "Doe",
                "Smith",
                "12345678A",
                "api_key_123"
        );
    }

    @Nested
    class AllowedPathsTests {

        @Test
        @DisplayName("Should allow OPTIONS requests without authentication")
        void testOptionsRequestAllowed() throws IOException, ServletException {
            when(request.getRequestURI()).thenReturn("/api/some-endpoint");
            when(request.getMethod()).thenReturn("OPTIONS");

            jwtFilter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Should allow auth endpoints without authentication")
        void testAuthEndpointAllowed() throws IOException, ServletException {
            when(request.getRequestURI()).thenReturn("/api/auth/login");
            when(request.getMethod()).thenReturn("POST");

            jwtFilter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        @Test
        @DisplayName("Should allow auth register endpoint without authentication")
        void testAuthRegisterEndpointAllowed() throws IOException, ServletException {
            when(request.getRequestURI()).thenReturn("/api/auth/register");
            when(request.getMethod()).thenReturn("POST");

            jwtFilter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Nested
    class AuthorizationHeaderTests {

        @Test
        @DisplayName("Should return 401 when Authorization header is missing")
        void testMissingAuthorizationHeader() throws IOException, ServletException {
            when(request.getRequestURI()).thenReturn("/api/users");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeader("Authorization")).thenReturn(null);

            jwtFilter.doFilter(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(filterChain, never()).doFilter(request, response);
        }

        @Test
        @DisplayName("Should return 401 when Authorization header doesn't start with Bearer")
        void testInvalidAuthorizationHeader() throws IOException, ServletException {
            when(request.getRequestURI()).thenReturn("/api/users");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

            jwtFilter.doFilter(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(filterChain, never()).doFilter(request, response);
        }
    }

    @Nested
    class TokenValidationTests {

        @Test
        @DisplayName("Should proceed with valid token")
        void testValidToken() throws IOException, ServletException {
            String validToken = "valid.jwt.token";
            when(request.getRequestURI()).thenReturn("/api/users");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
            when(jwtService.getUserFromToken(validToken)).thenReturn(testUser);

            jwtFilter.doFilter(request, response, filterChain);

            verify(jwtService).validateToken(validToken);
            verify(jwtService).getUserFromToken(validToken);
            verify(request).setAttribute("userId", testUser.userId());
            verify(request).setAttribute("user", testUser);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should return 401 when token validation fails")
        void testInvalidToken() throws IOException, ServletException {
            String invalidToken = "invalid.jwt.token";
            when(request.getRequestURI()).thenReturn("/api/users");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
            doThrow(new JwtException("Invalid token")).when(jwtService).validateToken(invalidToken);

            jwtFilter.doFilter(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(filterChain, never()).doFilter(request, response);
        }

        @Test
        @DisplayName("Should return 401 when getUserFromToken throws JwtException")
        void testGetUserFromTokenFails() throws IOException, ServletException {
            String validToken = "valid.jwt.token";
            when(request.getRequestURI()).thenReturn("/api/users");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
            when(jwtService.getUserFromToken(validToken)).thenThrow(new JwtException("User not found"));

            jwtFilter.doFilter(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(filterChain, never()).doFilter(request, response);
        }
    }
}
