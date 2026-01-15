package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
import es.VetUp.banco_back.b_domain.repository.BankTransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankTransactionServiceImplTest {

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @InjectMocks
    private BankTransactionServiceImpl bankTransactionServiceImpl;

    private BankTransaction createTestTransaction(Long transactionId, Long accountId, Long cardId) {
        CreditCard creditCard = new CreditCard(cardId, "4111111111111111", "12/28", "123", "John Doe", accountId);
        BankAccount bankAccount = new BankAccount(accountId, "ES9121000418450200051332", new BigDecimal("1000.00"), 1L);
        return new BankTransaction(
                transactionId,
                BankTransactionType.Credit,
                OriginBankingMovement.Transfer,
                creditCard,
                LocalDate.of(2026, 1, 15),
                new BigDecimal("150.50"),
                "Test transaction",
                bankAccount
        );
    }

    @Nested
    class GetTransactionsByAccountIdTests {

        @Test
        @DisplayName("getTransactionsByAccountId should return list of transactions")
        void testGetTransactionsByAccountId() {
            Long accountId = 1L;

            BankTransaction transaction1 = createTestTransaction(1L, accountId, 1L);
            BankTransaction transaction2 = createTestTransaction(2L, accountId, 1L);

            List<BankTransaction> transactions = List.of(transaction1, transaction2);

            when(bankTransactionRepository.findAllByAccountId(accountId)).thenReturn(transactions);

            List<BankTransaction> result = bankTransactionServiceImpl.getTransactionsByAccountId(accountId);

            assertAll("result",
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(2, result.size(), "Result size should be 2"),
                    () -> assertEquals(1L, result.get(0).getTransactionId()),
                    () -> assertEquals(2L, result.get(1).getTransactionId()));
        }

        @Test
        @DisplayName("getTransactionsByAccountId should return empty list when no transactions found")
        void testGetTransactionsByAccountIdEmpty() {
            Long accountId = 99L;

            when(bankTransactionRepository.findAllByAccountId(accountId)).thenReturn(List.of());

            List<BankTransaction> result = bankTransactionServiceImpl.getTransactionsByAccountId(accountId);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(0, result.size()));
        }
    }

    @Nested
    class GetAllTransactionsByCardIdTests {

        @Test
        @DisplayName("getAllTransactionsByCardId should return list of transactions")
        void testGetAllTransactionsByCardId() {
            Long cardId = 1L;

            BankTransaction transaction1 = createTestTransaction(1L, 1L, cardId);
            BankTransaction transaction2 = createTestTransaction(2L, 1L, cardId);

            List<BankTransaction> transactions = List.of(transaction1, transaction2);

            when(bankTransactionRepository.findAllByCardId(cardId)).thenReturn(transactions);

            List<BankTransaction> result = bankTransactionServiceImpl.getAllTransactionsByCardId(cardId);

            assertAll("result",
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(2, result.size(), "Result size should be 2"),
                    () -> assertEquals(1L, result.get(0).getTransactionId()),
                    () -> assertEquals(2L, result.get(1).getTransactionId()));
        }

        @Test
        @DisplayName("getAllTransactionsByCardId should return empty list when no transactions found")
        void testGetAllTransactionsByCardIdEmpty() {
            Long cardId = 99L;

            when(bankTransactionRepository.findAllByCardId(cardId)).thenReturn(List.of());

            List<BankTransaction> result = bankTransactionServiceImpl.getAllTransactionsByCardId(cardId);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(0, result.size()));
        }
    }

    @Nested
    class GetTransactionsBetweenDatesTests {

        @Test
        @DisplayName("getTransactionsBetweenDates should return list of transactions within date range")
        void testGetTransactionsBetweenDates() {
            Long cardId = 1L;
            LocalDate startDate = LocalDate.of(2026, 1, 1);
            LocalDate endDate = LocalDate.of(2026, 1, 31);

            BankTransaction transaction1 = createTestTransaction(1L, 1L, cardId);
            BankTransaction transaction2 = createTestTransaction(2L, 1L, cardId);

            List<BankTransaction> transactions = List.of(transaction1, transaction2);

            when(bankTransactionRepository.findAllByCardIdAndDateBetween(cardId, startDate, endDate))
                    .thenReturn(transactions);

            List<BankTransaction> result = bankTransactionServiceImpl.getTransactionsBetweenDates(cardId, startDate, endDate);

            assertAll("result",
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(2, result.size(), "Result size should be 2"),
                    () -> assertEquals(1L, result.get(0).getTransactionId()),
                    () -> assertEquals(2L, result.get(1).getTransactionId()));
        }

        @Test
        @DisplayName("getTransactionsBetweenDates should return empty list when no transactions found in date range")
        void testGetTransactionsBetweenDatesEmpty() {
            Long cardId = 1L;
            LocalDate startDate = LocalDate.of(2025, 1, 1);
            LocalDate endDate = LocalDate.of(2025, 1, 31);

            when(bankTransactionRepository.findAllByCardIdAndDateBetween(cardId, startDate, endDate))
                    .thenReturn(List.of());

            List<BankTransaction> result = bankTransactionServiceImpl.getTransactionsBetweenDates(cardId, startDate, endDate);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(0, result.size()));
        }
    }

    @Nested
    class GetAllTransactionsTests {

        @Test
        @DisplayName("getAllTransactions should return list of all transactions")
        void testGetAllTransactions() {
            BankTransaction transaction1 = createTestTransaction(1L, 1L, 1L);
            BankTransaction transaction2 = createTestTransaction(2L, 2L, 2L);

            List<BankTransaction> transactions = List.of(transaction1, transaction2);

            when(bankTransactionRepository.findAll()).thenReturn(transactions);

            List<BankTransaction> result = bankTransactionServiceImpl.getAllTransactions();

            assertAll("result",
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(2, result.size(), "Result size should be 2"),
                    () -> assertEquals(1L, result.get(0).getTransactionId()),
                    () -> assertEquals(2L, result.get(1).getTransactionId()));
        }

        @Test
        @DisplayName("getAllTransactions should return empty list when no transactions exist")
        void testGetAllTransactionsEmpty() {
            when(bankTransactionRepository.findAll()).thenReturn(List.of());

            List<BankTransaction> result = bankTransactionServiceImpl.getAllTransactions();

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(0, result.size()));
        }
    }

    @Nested
    class GetTransactionByIdTests {

        @Test
        @DisplayName("getTransactionById should return transaction when found")
        void testGetTransactionById() {
            Long transactionId = 1L;

            BankTransaction transaction = createTestTransaction(transactionId, 1L, 1L);

            when(bankTransactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

            Optional<BankTransaction> result = bankTransactionServiceImpl.getTransactionById(transactionId);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(transactionId, result.get().getTransactionId()),
                    () -> assertEquals(BankTransactionType.Credit, result.get().getType()));
        }

        @Test
        @DisplayName("getTransactionById should return empty when transaction not found")
        void testGetTransactionByIdNotFound() {
            Long transactionId = 99L;

            when(bankTransactionRepository.findById(transactionId)).thenReturn(Optional.empty());

            Optional<BankTransaction> result = bankTransactionServiceImpl.getTransactionById(transactionId);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class CreateTransactionTests {

        @Test
        @DisplayName("createTransaction should create and return transaction")
        void testCreateTransaction() {
            CreditCard creditCard = new CreditCard(1L, "4111111111111111", "12/28", "123", "John Doe", 1L);
            BankAccount bankAccount = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1000.00"), 1L);

            BankTransaction transactionToCreate = new BankTransaction(
                    null,
                    BankTransactionType.Credit,
                    OriginBankingMovement.Transfer,
                    creditCard,
                    LocalDate.of(2026, 1, 15),
                    new BigDecimal("150.50"),
                    "Salary deposit",
                    bankAccount
            );

            BankTransaction createdTransaction = new BankTransaction(
                    1L,
                    BankTransactionType.Credit,
                    OriginBankingMovement.Transfer,
                    creditCard,
                    LocalDate.of(2026, 1, 15),
                    new BigDecimal("150.50"),
                    "Salary deposit",
                    bankAccount
            );

            when(bankTransactionRepository.save(transactionToCreate)).thenReturn(createdTransaction);

            BankTransaction result = bankTransactionServiceImpl.createTransaction(transactionToCreate);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(1L, result.getTransactionId()),
                    () -> assertEquals(BankTransactionType.Credit, result.getType()),
                    () -> assertEquals(OriginBankingMovement.Transfer, result.getOrigin()),
                    () -> assertEquals(0, new BigDecimal("150.50").compareTo(result.getAmount())),
                    () -> assertEquals("Salary deposit", result.getDescription()));
        }

        @Test
        @DisplayName("createTransaction should create debit transaction")
        void testCreateDebitTransaction() {
            CreditCard creditCard = new CreditCard(1L, "5500000000000004", "06/27", "456", "Jane Smith", 1L);
            BankAccount bankAccount = new BankAccount(1L, "ES7620770024003102575766", new BigDecimal("500.00"), 1L);

            BankTransaction transactionToCreate = new BankTransaction(
                    null,
                    BankTransactionType.Debit,
                    OriginBankingMovement.BankCard,
                    creditCard,
                    LocalDate.of(2026, 1, 10),
                    new BigDecimal("75.25"),
                    "Online purchase",
                    bankAccount
            );

            BankTransaction createdTransaction = new BankTransaction(
                    2L,
                    BankTransactionType.Debit,
                    OriginBankingMovement.BankCard,
                    creditCard,
                    LocalDate.of(2026, 1, 10),
                    new BigDecimal("75.25"),
                    "Online purchase",
                    bankAccount
            );

            when(bankTransactionRepository.save(transactionToCreate)).thenReturn(createdTransaction);

            BankTransaction result = bankTransactionServiceImpl.createTransaction(transactionToCreate);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(2L, result.getTransactionId()),
                    () -> assertEquals(BankTransactionType.Debit, result.getType()),
                    () -> assertEquals(OriginBankingMovement.BankCard, result.getOrigin()),
                    () -> assertEquals(0, new BigDecimal("75.25").compareTo(result.getAmount())),
                    () -> assertEquals("Online purchase", result.getDescription()));
        }
    }
}
