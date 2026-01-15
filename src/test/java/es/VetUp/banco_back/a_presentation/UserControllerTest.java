package es.VetUp.banco_back.a_presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.VetUp.banco_back.a_presentation.webModel.request.ApiKeyRequest;
import es.VetUp.banco_back.b_domain.service.JwtService;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto createTestUserDto() {
        return new UserDto(
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
    class GetUserByUsernameTests {

        @Test
        @DisplayName("GET /api/users/by-username/{username} should return user")
        void testGetUserByUsername() throws Exception {
            UserDto userDto = createTestUserDto();
            when(userService.findByUsername("johndoe")).thenReturn(Optional.of(userDto));

            mockMvc.perform(get("/api/users/by-username/johndoe"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(1L))
                    .andExpect(jsonPath("$.username").value("johndoe"))
                    .andExpect(jsonPath("$.name").value("John"))
                    .andExpect(jsonPath("$.firstSurname").value("Doe"))
                    .andExpect(jsonPath("$.secondSurname").value("Smith"))
                    .andExpect(jsonPath("$.dni").value("12345678A"));
        }
    }

    @Nested
    class GetUserByDniTests {

        @Test
        @DisplayName("GET /api/users/by-dni/{dni} should return user")
        void testGetUserByDni() throws Exception {
            UserDto userDto = createTestUserDto();
            when(userService.findByDni("12345678A")).thenReturn(Optional.of(userDto));

            mockMvc.perform(get("/api/users/by-dni/12345678A"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(1L))
                    .andExpect(jsonPath("$.username").value("johndoe"))
                    .andExpect(jsonPath("$.dni").value("12345678A"));
        }
    }

    @Nested
    class GetUserByIdTests {

        @Test
        @DisplayName("GET /api/users/{id} should return user")
        void testGetUserById() throws Exception {
            UserDto userDto = createTestUserDto();
            when(userService.findById(1L)).thenReturn(Optional.of(userDto));

            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(1L))
                    .andExpect(jsonPath("$.username").value("johndoe"));
        }
    }

    @Nested
    class CheckApiKeyTests {

        @Test
        @DisplayName("POST /api/users should return OK when credentials are valid")
        void testCheckApiKeyValid() throws Exception {
            when(userService.authenticate("johndoe", "api_key_123")).thenReturn(true);

            ApiKeyRequest request = new ApiKeyRequest("api_key_123", "johndoe");

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("POST /api/users should return UNAUTHORIZED when credentials are invalid")
        void testCheckApiKeyInvalid() throws Exception {
            when(userService.authenticate("johndoe", "wrong_key")).thenReturn(false);

            ApiKeyRequest request = new ApiKeyRequest("wrong_key", "johndoe");

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }
}
