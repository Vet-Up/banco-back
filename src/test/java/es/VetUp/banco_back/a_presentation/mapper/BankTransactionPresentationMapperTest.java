package es.VetUp.banco_back.a_presentation.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.VetUp.banco_back.a_presentation.webModel.response.BankTransactionResponse;
import es.VetUp.banco_back.a_presentation.webModel.response.summary.BankTransactionSummaryResponse;
import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;

class BankTransactionPresentationMapperTest {

    @Test
    @DisplayName("Test map from BankTransaction to BankTransactionResponse")
    void testFromBankTransactionToBankTransactionResponse() {
        BankAccount bankAccount = new BankAccount(
                1L,
                "ES7620770024003102575766",
                new BigDecimal("1500.00"),
                null);

        CreditCard creditCard = new CreditCard(
                1L,
                "4111111111111111",
                "2027-12-31",
                "123",
                "John Doe Smith",
                1L);

        BankTransaction bankTransaction = new BankTransaction(
                1L,
                BankTransactionType.Debit,
                OriginBankingMovement.BankCard,
                creditCard,
                LocalDateTime.of(2026, 1, 7, 10, 0),
                new BigDecimal("50.00"),
                "Grocery store purchase",
                bankAccount);

        BankTransactionResponse response = BankTransactionPresentationMapper
                .fromBankTransactionToBankTransactionResponse(bankTransaction);

        assertEquals(bankTransaction.getTransactionId(), response.transactionId());
        assertEquals(bankTransaction.getType().toString(), response.transactionType());
        assertEquals(bankTransaction.getOrigin().toString(), response.originType());
        assertEquals(bankTransaction.getDate(), response.date());
        assertEquals(bankTransaction.getAmount(), response.amount());
        assertEquals(bankTransaction.getDescription(), response.description());
        assertNotNull(response.creditCard());
    }

    @Test
    @DisplayName("Test map from BankTransaction to BankTransactionResponse with null")
    void testFromBankTransactionToBankTransactionResponseNull() {
        BankTransactionResponse response = BankTransactionPresentationMapper
                .fromBankTransactionToBankTransactionResponse(null);

        assertNull(response);
    }

    @Test
    @DisplayName("Test map from BankTransaction to BankTransactionSummaryResponse")
    void testFromBankTransactionToBankTransactionSummaryResponse() {
        BankAccount bankAccount = new BankAccount(
                2L,
                "ES1200492352123456789012",
                new BigDecimal("3200.50"),
                null);

        CreditCard creditCard = new CreditCard(
                2L,
                "5500000000000004",
                "2028-05-31",
                "456",
                "Michael Roberts Johnson",
                2L);

        BankTransaction bankTransaction = new BankTransaction(
                2L,
                BankTransactionType.Credit,
                OriginBankingMovement.Transfer,
                creditCard,
                LocalDateTime.of(2026, 1, 6, 15, 30),
                new BigDecimal("200.00"),
                "Salary deposit",
                bankAccount);

        BankTransactionSummaryResponse response = BankTransactionPresentationMapper
                .fromBankTransactionToBankTransactionSummaryResponse(bankTransaction);

        assertEquals(bankTransaction.getTransactionId(), response.transactionId());
        assertEquals(bankTransaction.getType().toString(), response.transactionType());
        assertEquals(bankTransaction.getOrigin().toString(), response.originType());
        assertEquals(bankTransaction.getDate(), response.date());
        assertEquals(bankTransaction.getAmount(), response.amount());
        assertEquals(bankTransaction.getDescription(), response.description());
    }

    @Test
    @DisplayName("Test map from BankTransaction to BankTransactionSummaryResponse with null")
    void testFromBankTransactionToBankTransactionSummaryResponseNull() {
        BankTransactionSummaryResponse response = BankTransactionPresentationMapper
                .fromBankTransactionToBankTransactionSummaryResponse(null);

        assertNull(response);
    }
}