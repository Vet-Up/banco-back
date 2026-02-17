package es.VetUp.banco_back.a_presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.service.BankAccountService;
import es.VetUp.banco_back.b_domain.service.CardPaymentService;
import es.VetUp.banco_back.b_domain.service.JwtService;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentRequest;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentResponse;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Authorization;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Destination;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Origin;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Pay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankAccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class BankAccountControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private BankAccountService bankAccountService;

        @MockitoBean
        private CardPaymentService cardPaymentService;

        @MockitoBean
        private JwtService jwtService;

        @Autowired
        private ObjectMapper objectMapper;

        @Nested
        class GetBankAccountsByUserIdTests {

                @Test
                @DisplayName("GET /api/bank-accounts/by-user/{userId} should return list of bank accounts")
                void testGetBankAccountsByUserIdSuccess() throws Exception {
                        BankAccount account1 = new BankAccount(
                                        1L,
                                        "ES7620770024003102575766",
                                        new BigDecimal("1500.00"),
                                        1L);

                        BankAccount account2 = new BankAccount(
                                        2L,
                                        "ES1234567890123456789012",
                                        new BigDecimal("2500.50"),
                                        1L);

                        when(bankAccountService.findByUserId(1L)).thenReturn(List.of(account1, account2));

                        mockMvc.perform(get("/api/bank-accounts/by-user/{userId}", 1L))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.length()").value(2))
                                        .andExpect(jsonPath("$[0].accountId").value(1))
                                        .andExpect(jsonPath("$[0].iban").value("ES7620770024003102575766"))
                                        .andExpect(jsonPath("$[0].balance").value(1500.00))
                                        .andExpect(jsonPath("$[0].userId").value(1))
                                        .andExpect(jsonPath("$[1].accountId").value(2))
                                        .andExpect(jsonPath("$[1].iban").value("ES1234567890123456789012"))
                                        .andExpect(jsonPath("$[1].balance").value(2500.50))
                                        .andExpect(jsonPath("$[1].userId").value(1));
                }

                @Test
                @DisplayName("GET /api/bank-accounts/by-user/{userId} should return empty list when no accounts found")
                void testGetBankAccountsByUserIdEmpty() throws Exception {
                        when(bankAccountService.findByUserId(999L)).thenReturn(List.of());

                        mockMvc.perform(get("/api/bank-accounts/by-user/{userId}", 999L))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.length()").value(0));
                }
        }

        @Nested
        class ProcessCardPaymentTests {

                @Test
                @DisplayName("POST /api/bank-accounts/pago_tarjeta should process payment successfully")
                void testProcessCardPaymentSuccess() throws Exception {
                        CardPaymentRequest request = new CardPaymentRequest(
                                        new Authorization("testuser", "test_api_key"),
                                        new Origin("1234567890123456", "12/28", "123", "John Doe"),
                                        new Destination("ES1234567890123456789012"),
                                        new Pay(100.00, "Test payment"));

                        CardPaymentResponse expectedResponse = new CardPaymentResponse(
                                        "TXN-001",
                                        "SUCCESS",
                                        "Payment processed successfully");

                        when(cardPaymentService.processPayment(any(CardPaymentRequest.class)))
                                        .thenReturn(expectedResponse);

                        mockMvc.perform(post("/api/bank-accounts/pago_tarjeta")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.transactionId").value("TXN-001"))
                                        .andExpect(jsonPath("$.status").value("SUCCESS"))
                                        .andExpect(jsonPath("$.message").value("Payment processed successfully"));
                }

                @Test
                @DisplayName("POST /api/bank-accounts/pago_tarjeta should handle different amounts")
                void testProcessCardPaymentDifferentAmounts() throws Exception {
                        CardPaymentRequest request = new CardPaymentRequest(
                                        new Authorization("user", "api_key"),
                                        new Origin("9876543210987654", "06/30", "456", "Jane Smith"),
                                        new Destination("ES9999999999999999999999"),
                                        new Pay(500.50, "Large payment"));

                        CardPaymentResponse expectedResponse = new CardPaymentResponse(
                                        "TXN-002",
                                        "SUCCESS",
                                        "Payment completed");

                        when(cardPaymentService.processPayment(any(CardPaymentRequest.class)))
                                        .thenReturn(expectedResponse);

                        mockMvc.perform(post("/api/bank-accounts/pago_tarjeta")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.status").value("SUCCESS"))
                                        .andExpect(jsonPath("$.message").value("Payment completed"));
                }
        }
}
