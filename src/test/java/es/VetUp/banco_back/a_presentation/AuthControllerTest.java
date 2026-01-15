package es.VetUp.banco_back.a_presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.VetUp.banco_back.a_presentation.webModel.request.LoginRequest;
import es.VetUp.banco_back.b_domain.service.JwtService;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto createTestUserDto() {
        return new UserDto(
                1L,
                "johndoe",
                "encodedPassword123",
                "John",
                "Doe",
                "Smith",
                "12345678A",
                "api_key_123"
        );
    }

    @Nested
    @DisplayName("POST /api/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Should return token when credentials are valid")
        void testLoginSuccess() throws Exception {
            UserDto userDto = createTestUserDto();
            LoginRequest loginRequest = new LoginRequest("johndoe", "password123");

            when(userService.findByUsername("johndoe")).thenReturn(Optional.of(userDto));
            when(passwordEncoder.matches("password123", "encodedPassword123")).thenReturn(true);
            when(jwtService.generateToken(any(UserDto.class))).thenReturn("jwt_token_12345");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt_token_12345"));
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void testLoginUserNotFound() {
            LoginRequest loginRequest = new LoginRequest("unknownuser", "password123");

            when(userService.findByUsername("unknownuser")).thenReturn(Optional.empty());

            org.junit.jupiter.api.Assertions.assertThrows(ServletException.class, () ->
                    mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
            );
        }

        @Test
        @DisplayName("Should throw exception when password is invalid")
        void testLoginInvalidPassword() {
            UserDto userDto = createTestUserDto();
            LoginRequest loginRequest = new LoginRequest("johndoe", "wrongpassword");

            when(userService.findByUsername("johndoe")).thenReturn(Optional.of(userDto));
            when(passwordEncoder.matches("wrongpassword", "encodedPassword123")).thenReturn(false);

            org.junit.jupiter.api.Assertions.assertThrows(ServletException.class, () ->
                    mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
            );
        }
    }

    @Nested
    @DisplayName("GET /api/auth/validate")
    class ValidateTokenTests {

        @Test
        @DisplayName("Should return user details when token is valid")
        void testValidateTokenSuccess() throws Exception {
            UserDto userDto = createTestUserDto();

            when(jwtService.getUserFromToken("valid_token_123")).thenReturn(userDto);

            mockMvc.perform(get("/api/auth/validate")
                            .header("Authorization", "Bearer valid_token_123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(1))
                    .andExpect(jsonPath("$.username").value("johndoe"))
                    .andExpect(jsonPath("$.name").value("John"))
                    .andExpect(jsonPath("$.firstSurname").value("Doe"))
                    .andExpect(jsonPath("$.secondSurname").value("Smith"))
                    .andExpect(jsonPath("$.dni").value("12345678A"));
        }

        @Test
        @DisplayName("Should throw exception when token is invalid")
        void testValidateTokenInvalid() {
            when(jwtService.getUserFromToken("invalid_token")).thenThrow(new RuntimeException("Invalid token"));

            org.junit.jupiter.api.Assertions.assertThrows(ServletException.class, () ->
                    mockMvc.perform(get("/api/auth/validate")
                            .header("Authorization", "Bearer invalid_token"))
            );
        }

        @Test
        @DisplayName("Should fail when Authorization header is missing")
        void testValidateTokenMissingHeader() throws Exception {
            mockMvc.perform(get("/api/auth/validate"))
                    .andExpect(status().isBadRequest());
        }
    }
}
