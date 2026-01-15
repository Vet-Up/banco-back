package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.exception.ResourceNotFoundException;
import es.VetUp.banco_back.b_domain.mapper.UserMapper;
import es.VetUp.banco_back.b_domain.repository.UserRepository;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    private UserEntity testUserEntity;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        testUserEntity = new UserEntity(
                1L,
                "johndoe",
                "password123",
                "John",
                "Doe",
                "Smith",
                "12345678A",
                "api_key_123"
        );
    }

    @Nested
    class AuthenticateTests {

        @Test
        @DisplayName("authenticate should return true when credentials are valid")
        void testAuthenticateSuccess() {
            String username = "johndoe";
            String apiKey = "api_key_123";
            when(userRepository.existsByApiKey(username, apiKey)).thenReturn(Optional.of(true));

            Boolean result = userService.authenticate(username, apiKey);

            assertTrue(result);
        }

        @Test
        @DisplayName("authenticate should return false when credentials are invalid")
        void testAuthenticateInvalidCredentials() {
            String username = "johndoe";
            String apiKey = "wrong_api_key";
            when(userRepository.existsByApiKey(username, apiKey)).thenReturn(Optional.of(false));

            Boolean result = userService.authenticate(username, apiKey);

            assertFalse(result);
        }

        @Test
        @DisplayName("authenticate should throw exception when user not found")
        void testAuthenticateUserNotFound() {
            String username = "nonexistent";
            String apiKey = "api_key";
            when(userRepository.existsByApiKey(username, apiKey)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> userService.authenticate(username, apiKey));
        }
    }

    @Nested
    class FindByUsernameTests {

        @Test
        @DisplayName("findByUsername should return user when found")
        void testFindByUsername() {
            String username = "johndoe";
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUserEntity));

            Optional<UserDto> result = userService.findByUsername(username);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testUserEntity.userId(), result.get().userId()),
                    () -> assertEquals(testUserEntity.username(), result.get().username())
            );
        }

        @Test
        @DisplayName("findByUsername should return empty when not found")
        void testFindByUsernameNotFound() {
            String username = "nonexistent";
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

            Optional<UserDto> result = userService.findByUsername(username);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class FindByDniTests {

        @Test
        @DisplayName("findByDni should return user when found")
        void testFindByDni() {
            String dni = "12345678A";
            when(userRepository.findByDni(dni)).thenReturn(Optional.of(testUserEntity));

            Optional<UserDto> result = userService.findByDni(dni);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testUserEntity.userId(), result.get().userId()),
                    () -> assertEquals(testUserEntity.dni(), result.get().dni())
            );
        }

        @Test
        @DisplayName("findByDni should return empty when not found")
        void testFindByDniNotFound() {
            String dni = "00000000X";
            when(userRepository.findByDni(dni)).thenReturn(Optional.empty());

            Optional<UserDto> result = userService.findByDni(dni);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        @DisplayName("findById should return user when found")
        void testFindById() {
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.of(testUserEntity));

            Optional<UserDto> result = userService.findById(userId);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testUserEntity.userId(), result.get().userId()),
                    () -> assertEquals(testUserEntity.username(), result.get().username())
            );
        }

        @Test
        @DisplayName("findById should return empty when not found")
        void testFindByIdNotFound() {
            Long userId = 99L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            Optional<UserDto> result = userService.findById(userId);

            assertFalse(result.isPresent());
        }
    }
}
