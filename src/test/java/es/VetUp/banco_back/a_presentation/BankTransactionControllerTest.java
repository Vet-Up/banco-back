package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
import es.VetUp.banco_back.b_domain.service.BankTransactionService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class BankTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankTransactionService bankTransactionService;

    @MockitoBean
    private JwtService jwtService;

    private BankTransaction transaction1;
    private BankTransaction transaction2;
    private BankAccount bankAccount;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount(
                1L,
                "ES7620770024003102575766",
                new BigDecimal("1500.00"),
                null);

        creditCard = new CreditCard(
                1L,
                "4111111111111111",
                "2027-12-31",
                "123",
                "John Doe Smith",
                1L);

        transaction1 = new BankTransaction(
                1L,
                BankTransactionType.Debit,
                OriginBankingMovement.BankCard,
                creditCard,
                LocalDate.of(2026, 1, 7),
                new BigDecimal("50.00"),
                "Grocery store purchase",
                bankAccount);

        transaction2 = new BankTransaction(
                2L,
                BankTransactionType.Credit,
                OriginBankingMovement.Transfer,
                creditCard,
                LocalDate.of(2026, 1, 6),
                new BigDecimal("200.00"),
                "Salary deposit",
                bankAccount);
    }

    @Nested
    class GetByIdTests {
        @Test
        @DisplayName("GET /api/bank-transactions/{id} - Success")
        void testGetBankTransactionByIdSuccess() throws Exception {
            when(bankTransactionService.getTransactionById(1L)).thenReturn(Optional.of(transaction1));

            mockMvc.perform(get("/api/bank-transactions/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.transactionId").value(1))
                    .andExpect(jsonPath("$.transactionType").value("Debit"))
                    .andExpect(jsonPath("$.originType").value("BankCard"))
                    .andExpect(jsonPath("$.amount").value(50.00))
                    .andExpect(jsonPath("$.description").value("Grocery store purchase"));
        }
    }

    @Test
    @DisplayName("GET /api/bank-transactions/by-account/{accountId} - Success")
    void testGetTransactionsByAccountIdSuccess() throws Exception {
        when(bankTransactionService.getTransactionsByAccountId(1L)).thenReturn(List.of(transaction1, transaction2));

        mockMvc.perform(get("/api/bank-transactions/by-account/{accountId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].transactionId").value(1))
                .andExpect(jsonPath("$[0].description").value("Grocery store purchase"))
                .andExpect(jsonPath("$[1].transactionId").value(2))
                .andExpect(jsonPath("$[1].description").value("Salary deposit"));
    }

}