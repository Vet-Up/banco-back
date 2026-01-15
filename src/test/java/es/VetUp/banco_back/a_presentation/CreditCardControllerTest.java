package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.service.CreditCardService;
import es.VetUp.banco_back.b_domain.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditCardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreditCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreditCardService creditCardService;

    @MockitoBean
    private JwtService jwtService;

    private CreditCard creditCard1;
    private CreditCard creditCard2;

    @BeforeEach
    void setUp() {
        creditCard1 = new CreditCard(
                1L,
                "4111111111111111",
                "2027-12-31",
                "123",
                "John Doe Smith",
                1L);

        creditCard2 = new CreditCard(
                2L,
                "5500000000000004",
                "2028-06-30",
                "456",
                "Jane Smith Roberts",
                2L);
    }

    @Nested
    class GetByIdTests {
        @Test
        @DisplayName("GET /api/credit-cards/{id} - Success")
        void testGetCreditCardByIdSuccess() throws Exception {
            when(creditCardService.getCreditCardById(1L)).thenReturn(creditCard1);

            mockMvc.perform(get("/api/credit-cards/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.creditCardId").value(1))
                    .andExpect(jsonPath("$.cardNumber").value("4111111111111111"))
                    .andExpect(jsonPath("$.expirationDate").value("2027-12-31"))
                    .andExpect(jsonPath("$.cvv").value("123"))
                    .andExpect(jsonPath("$.fullName").value("John Doe Smith"));
        }

    }
}
