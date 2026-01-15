package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private UserService userService;

    private JwtServiceImpl jwtService;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(userService);
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
    class GenerateTokenTests {

        @Test
        @DisplayName("generateToken should return a valid JWT token")
        void testGenerateToken() {
            String token = jwtService.generateToken(testUser);

            assertNotNull(token);
            assertFalse(token.isEmpty());
            assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
        }

        @Test
        @DisplayName("generateToken should include user id in claims")
        void testGenerateTokenContainsUserId() {
            String token = jwtService.generateToken(testUser);

            Claims claims = jwtService.validateToken(token);

            assertEquals(testUser.userId(), claims.get("id", Long.class));
        }

        @Test
        @DisplayName("generateToken with different users should produce different tokens")
        void testGenerateTokenDifferentUsers() {
            UserDto anotherUser = new UserDto(
                    2L,
                    "anotheruser",
                    "password456",
                    "Jane",
                    "Smith",
                    "Roberts",
                    "87654321B",
                    "api_key_456"
            );

            String token1 = jwtService.generateToken(testUser);
            String token2 = jwtService.generateToken(anotherUser);

            assertNotEquals(token1, token2);
        }
    }

    @Nested
    class ValidateTokenTests {

        @Test
        @DisplayName("validateToken should return claims for valid token")
        void testValidateToken() {
            String token = jwtService.generateToken(testUser);

            Claims claims = jwtService.validateToken(token);

            assertNotNull(claims);
            assertEquals(testUser.userId(), claims.get("id", Long.class));
        }

        @Test
        @DisplayName("validateToken should throw exception for invalid token")
        void testValidateTokenInvalid() {
            String invalidToken = "invalid.token.here";

            assertThrows(MalformedJwtException.class, () -> jwtService.validateToken(invalidToken));
        }

        @Test
        @DisplayName("validateToken should throw exception for tampered token")
        void testValidateTokenTampered() {
            String token = jwtService.generateToken(testUser);
            String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

            assertThrows(Exception.class, () -> jwtService.validateToken(tamperedToken));
        }
    }

    @Nested
    class GetUserFromTokenTests {

        @Test
        @DisplayName("getUserFromToken should return user for valid token")
        void testGetUserFromToken() {
            String token = jwtService.generateToken(testUser);
            when(userService.findById(testUser.userId())).thenReturn(Optional.of(testUser));

            UserDto result = jwtService.getUserFromToken(token);

            assertNotNull(result);
            assertEquals(testUser.userId(), result.userId());
            assertEquals(testUser.username(), result.username());
        }

        @Test
        @DisplayName("getUserFromToken should throw exception when user not found")
        void testGetUserFromTokenUserNotFound() {
            String token = jwtService.generateToken(testUser);
            when(userService.findById(testUser.userId())).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> jwtService.getUserFromToken(token));
        }

        @Test
        @DisplayName("getUserFromToken should throw exception for invalid token")
        void testGetUserFromTokenInvalidToken() {
            String invalidToken = "invalid.token.here";

            assertThrows(Exception.class, () -> jwtService.getUserFromToken(invalidToken));
        }
    }
}
