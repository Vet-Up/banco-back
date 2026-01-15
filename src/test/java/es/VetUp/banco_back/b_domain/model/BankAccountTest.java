package es.VetUp.banco_back.b_domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    @DisplayName("Test BankAccount Creation")
    void testBankAccountCreation() {
        Long accountId = 1L;
        String iban = "ES9121000418450200051332";
        BigDecimal balance = new BigDecimal("1000.00");
        Long userId = 1L;

        BankAccount bankAccount = assertDoesNotThrow(() -> new BankAccount(accountId, iban, balance, userId));

        assertAll("bankAccount",
                () -> assertEquals(accountId, bankAccount.getAccountId()),
                () -> assertEquals(iban, bankAccount.getIban()),
                () -> assertEquals(0, balance.compareTo(bankAccount.getBalance())),
                () -> assertEquals(userId, bankAccount.getUserId())
        );
    }

    @Test
    @DisplayName("Test BankAccount Creation with different data")
    void testBankAccountCreationDifferentData() {
        Long accountId = 2L;
        String iban = "ES7620770024003102575766";
        BigDecimal balance = new BigDecimal("2500.50");
        Long userId = 2L;

        BankAccount bankAccount = assertDoesNotThrow(() -> new BankAccount(accountId, iban, balance, userId));

        assertAll("bankAccount",
                () -> assertEquals(accountId, bankAccount.getAccountId()),
                () -> assertEquals(iban, bankAccount.getIban()),
                () -> assertEquals(0, balance.compareTo(bankAccount.getBalance())),
                () -> assertEquals(userId, bankAccount.getUserId())
        );
    }

    @Test
    @DisplayName("Test BankAccount Creation with zero balance")
    void testBankAccountCreationZeroBalance() {
        Long accountId = 3L;
        String iban = "ES1234567890123456789012";
        BigDecimal balance = BigDecimal.ZERO;
        Long userId = 3L;

        BankAccount bankAccount = assertDoesNotThrow(() -> new BankAccount(accountId, iban, balance, userId));

        assertAll("bankAccount",
                () -> assertEquals(accountId, bankAccount.getAccountId()),
                () -> assertEquals(iban, bankAccount.getIban()),
                () -> assertEquals(0, BigDecimal.ZERO.compareTo(bankAccount.getBalance())),
                () -> assertEquals(userId, bankAccount.getUserId())
        );
    }

    @Test
    @DisplayName("Test BankAccount Creation with null userId")
    void testBankAccountCreationNullUserId() {
        Long accountId = 4L;
        String iban = "ES9876543210987654321098";
        BigDecimal balance = new BigDecimal("500.00");

        BankAccount bankAccount = assertDoesNotThrow(() -> new BankAccount(accountId, iban, balance, null));

        assertAll("bankAccount",
                () -> assertEquals(accountId, bankAccount.getAccountId()),
                () -> assertEquals(iban, bankAccount.getIban()),
                () -> assertNull(bankAccount.getUserId())
        );
    }
}
