package es.VetUp.banco_back.b_domain.model;

import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BankTransactionTest {

    @Test
    @DisplayName("Test BankTransaction Creation")
    void testBankTransactionCreation() {
        Long transactionId = 1L;
        BankTransactionType type = BankTransactionType.Credit;
        OriginBankingMovement origin = OriginBankingMovement.Transfer;
        CreditCard creditCard = new CreditCard(1L, "4111111111111111", "12/28", "123", "John Doe", 1L);
        LocalDateTime date = LocalDateTime.of(2026, 1, 15, 10, 0);
        BigDecimal amount = new BigDecimal("150.50");
        String description = "Salary deposit";
        BankAccount bankAccount = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1000.00"), 1L);

        BankTransaction bankTransaction = assertDoesNotThrow(() -> new BankTransaction(
                transactionId, type, origin, creditCard, date, amount, description, bankAccount));

        assertAll("bankTransaction",
                () -> assertEquals(transactionId, bankTransaction.getTransactionId()),
                () -> assertEquals(type, bankTransaction.getType()),
                () -> assertEquals(origin, bankTransaction.getOrigin()),
                () -> assertEquals(creditCard, bankTransaction.getCreditCard()),
                () -> assertEquals(date, bankTransaction.getDate()),
                () -> assertEquals(0, amount.compareTo(bankTransaction.getAmount())),
                () -> assertEquals(description, bankTransaction.getDescription()),
                () -> assertEquals(bankAccount, bankTransaction.getBankAccount())
        );
    }

    @Test
    @DisplayName("Test BankTransaction Creation with Debit Type")
    void testBankTransactionCreationWithDebitType() {
        Long transactionId = 2L;
        BankTransactionType type = BankTransactionType.Debit;
        OriginBankingMovement origin = OriginBankingMovement.BankCard;
        CreditCard creditCard = new CreditCard(2L, "5500000000000004", "06/27", "456", "Jane Smith", 2L);
        LocalDateTime date = LocalDateTime.of(2026, 1, 10, 14, 30);
        BigDecimal amount = new BigDecimal("75.25");
        String description = "Online purchase";
        BankAccount bankAccount = new BankAccount(2L, "ES7620770024003102575766", new BigDecimal("500.00"), 2L);

        BankTransaction bankTransaction = assertDoesNotThrow(() -> new BankTransaction(
                transactionId, type, origin, creditCard, date, amount, description, bankAccount));

        assertAll("bankTransaction",
                () -> assertEquals(transactionId, bankTransaction.getTransactionId()),
                () -> assertEquals(type, bankTransaction.getType()),
                () -> assertEquals(origin, bankTransaction.getOrigin()),
                () -> assertEquals(creditCard, bankTransaction.getCreditCard()),
                () -> assertEquals(date, bankTransaction.getDate()),
                () -> assertEquals(0, amount.compareTo(bankTransaction.getAmount())),
                () -> assertEquals(description, bankTransaction.getDescription()),
                () -> assertEquals(bankAccount, bankTransaction.getBankAccount())
        );
    }

    @Test
    @DisplayName("Test BankTransaction Creation with DirectDebit Origin")
    void testBankTransactionCreationWithDirectDebitOrigin() {
        Long transactionId = 3L;
        BankTransactionType type = BankTransactionType.Debit;
        OriginBankingMovement origin = OriginBankingMovement.DirectDebit;
        CreditCard creditCard = null;
        LocalDateTime date = LocalDateTime.of(2026, 1, 5, 9, 15);
        BigDecimal amount = new BigDecimal("99.99");
        String description = "Monthly subscription";
        BankAccount bankAccount = new BankAccount(3L, "ES1234567890123456789012", new BigDecimal("2000.00"), 3L);

        BankTransaction bankTransaction = assertDoesNotThrow(() -> new BankTransaction(
                transactionId, type, origin, creditCard, date, amount, description, bankAccount));

        assertAll("bankTransaction",
                () -> assertEquals(transactionId, bankTransaction.getTransactionId()),
                () -> assertEquals(type, bankTransaction.getType()),
                () -> assertEquals(origin, bankTransaction.getOrigin()),
                () -> assertNull(bankTransaction.getCreditCard()),
                () -> assertEquals(date, bankTransaction.getDate()),
                () -> assertEquals(0, amount.compareTo(bankTransaction.getAmount())),
                () -> assertEquals(description, bankTransaction.getDescription()),
                () -> assertEquals(bankAccount, bankTransaction.getBankAccount())
        );
    }


}