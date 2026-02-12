package es.VetUp.banco_back.c_persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankTransactionJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankTransactionJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankTransactionRepositoryImplTest {

    @Mock
    private BankTransactionJpaDao bankTransactionJpaDao;

    @InjectMocks
    private BankTransactionRepositoryImpl bankTransactionRepositoryImpl;

    BankTransactionJpaEntity transactionJpaEntity1;
    BankTransactionJpaEntity transactionJpaEntity2;
    BankAccountJpaEntity bankAccountJpaEntity;
    CreditCardJpaEntity creditCardJpaEntity;

    @BeforeEach
    void setUp() {
        bankAccountJpaEntity = new BankAccountJpaEntity();
        bankAccountJpaEntity.setAccountId(1L);
        bankAccountJpaEntity.setIban("ES9121000418450200051332");
        bankAccountJpaEntity.setBalance(new BigDecimal("1000.00"));

        creditCardJpaEntity = new CreditCardJpaEntity();
        creditCardJpaEntity.setSourceCardId(1L);
        creditCardJpaEntity.setCardNumber("4111111111111111");
        creditCardJpaEntity.setExpirationDate(LocalDate.of(2028, 12, 31));
        creditCardJpaEntity.setCvv("123");
        creditCardJpaEntity.setFullName("John Doe");
        creditCardJpaEntity.setBankAccount(bankAccountJpaEntity);

        transactionJpaEntity1 = new BankTransactionJpaEntity();
        transactionJpaEntity1.setTransactionId(1L);
        transactionJpaEntity1.setDate(LocalDateTime.of(2026, 1, 15, 10, 30, 0));
        transactionJpaEntity1.setAmount(new BigDecimal("150.50"));
        transactionJpaEntity1.setDescription("Salary deposit");
        transactionJpaEntity1.setTransactionTypeId(2L); // Credit
        transactionJpaEntity1.setTransactionOriginId(1L); // Transfer
        transactionJpaEntity1.setCreditCard(creditCardJpaEntity);
        transactionJpaEntity1.setBankAccount(bankAccountJpaEntity);

        transactionJpaEntity2 = new BankTransactionJpaEntity();
        transactionJpaEntity2.setTransactionId(2L);
        transactionJpaEntity2.setDate(LocalDateTime.of(2026, 1, 10, 14, 0, 0));
        transactionJpaEntity2.setAmount(new BigDecimal("75.25"));
        transactionJpaEntity2.setDescription("Online purchase");
        transactionJpaEntity2.setTransactionTypeId(1L); // Debit
        transactionJpaEntity2.setTransactionOriginId(3L); // BankCard
        transactionJpaEntity2.setCreditCard(creditCardJpaEntity);
        transactionJpaEntity2.setBankAccount(bankAccountJpaEntity);
    }

    @Nested
    class FindById {
        @Test
        @DisplayName("findById should return transaction when found")
        void testFindById() {
            when(bankTransactionJpaDao.findByTransactionId(1L)).thenReturn(Optional.of(transactionJpaEntity1));

            Optional<BankTransaction> actual = bankTransactionRepositoryImpl.findById(1L);

            assertAll(
                    () -> assertTrue(actual.isPresent()),
                    () -> assertEquals(1L, actual.get().getTransactionId()),
                    () -> assertEquals(BankTransactionType.Credit, actual.get().getType()),
                    () -> assertEquals(OriginBankingMovement.Transfer, actual.get().getOrigin()),
                    () -> assertEquals(LocalDateTime.of(2026, 1, 15, 10, 30, 0), actual.get().getDate()),
                    () -> assertEquals(0, new BigDecimal("150.50").compareTo(actual.get().getAmount())),
                    () -> assertEquals("Salary deposit", actual.get().getDescription()),
                    () -> assertNotNull(actual.get().getBankAccount()));
        }

        @Test
        @DisplayName("findById should return empty when not found")
        void testFindByIdNotFound() {
            when(bankTransactionJpaDao.findByTransactionId(99L)).thenReturn(Optional.empty());

            Optional<BankTransaction> actual = bankTransactionRepositoryImpl.findById(99L);

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    class FindAll {
        @Test
        @DisplayName("findAll should return all transactions")
        void testFindAll() {
            List<BankTransactionJpaEntity> expectedList = List.of(transactionJpaEntity1, transactionJpaEntity2);

            when(bankTransactionJpaDao.findAll()).thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl.findAll();

            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(1L, actual.get(0).getTransactionId()),
                    () -> assertEquals(2L, actual.get(1).getTransactionId()),
                    () -> assertEquals(BankTransactionType.Credit, actual.get(0).getType()),
                    () -> assertEquals(BankTransactionType.Debit, actual.get(1).getType()));
        }

        @Test
        @DisplayName("findAll should return empty list when no transactions exist")
        void testFindAllEmpty() {
            List<BankTransactionJpaEntity> expectedList = List.of();

            when(bankTransactionJpaDao.findAll()).thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl.findAll();

            assertAll(
                    () -> assertEquals(0, actual.size()),
                    () -> assertTrue(actual.isEmpty()));
        }
    }

    @Nested
    class FindAllByAccountId {
        @Test
        @DisplayName("findAllByAccountId should return transactions for given account")
        void testFindAllByAccountId() {
            Long accountId = 1L;
            List<BankTransactionJpaEntity> expectedList = List.of(transactionJpaEntity1, transactionJpaEntity2);

            when(bankTransactionJpaDao.findAllTransactionsByAccountId(accountId)).thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl.findAllByAccountId(accountId);

            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(1L, actual.get(0).getTransactionId()),
                    () -> assertEquals(2L, actual.get(1).getTransactionId()));
        }

        @Test
        @DisplayName("findAllByAccountId should return empty list when no transactions found")
        void testFindAllByAccountIdEmpty() {
            Long accountId = 99L;
            List<BankTransactionJpaEntity> expectedList = List.of();

            when(bankTransactionJpaDao.findAllTransactionsByAccountId(accountId)).thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl.findAllByAccountId(accountId);

            assertAll(
                    () -> assertEquals(0, actual.size()),
                    () -> assertTrue(actual.isEmpty()));
        }
    }

    @Nested
    class FindAllByCardId {
        @Test
        @DisplayName("findAllByCardId should return transactions for given card")
        void testFindAllByCardId() {
            Long cardId = 1L;
            List<BankTransactionJpaEntity> expectedList = List.of(transactionJpaEntity1, transactionJpaEntity2);

            when(bankTransactionJpaDao.findAllByCardId(cardId)).thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl.findAllByCardId(cardId);

            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(1L, actual.get(0).getTransactionId()),
                    () -> assertEquals(2L, actual.get(1).getTransactionId()));
        }

        @Test
        @DisplayName("findAllByCardId should return empty list when no transactions found")
        void testFindAllByCardIdEmpty() {
            Long cardId = 99L;
            List<BankTransactionJpaEntity> expectedList = List.of();

            when(bankTransactionJpaDao.findAllByCardId(cardId)).thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl.findAllByCardId(cardId);

            assertAll(
                    () -> assertEquals(0, actual.size()),
                    () -> assertTrue(actual.isEmpty()));
        }
    }

    @Nested
    class FindAllByCardIdAndDateBetween {
        @Test
        @DisplayName("findAllByCardIdAndDateBetween should return transactions within date range")
        void testFindAllByCardIdAndDateBetween() {
            Long cardId = 1L;
            LocalDate startDate = LocalDate.of(2026, 1, 1);
            LocalDate endDate = LocalDate.of(2026, 1, 31);
            List<BankTransactionJpaEntity> expectedList = List.of(transactionJpaEntity1, transactionJpaEntity2);

            when(bankTransactionJpaDao.findAllByCardIdAndDateBetween(cardId, startDate, endDate))
                    .thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl
                    .findAllByCardIdAndDateBetween(cardId, startDate, endDate);

            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(1L, actual.get(0).getTransactionId()),
                    () -> assertEquals(2L, actual.get(1).getTransactionId()),
                    () -> assertEquals(LocalDateTime.of(2026, 1, 15, 10, 30, 0), actual.get(0).getDate()),
                    () -> assertEquals(LocalDateTime.of(2026, 1, 10, 14, 0, 0), actual.get(1).getDate()));
        }

        @Test
        @DisplayName("findAllByCardIdAndDateBetween should return empty list when no transactions in date range")
        void testFindAllByCardIdAndDateBetweenEmpty() {
            Long cardId = 1L;
            LocalDate startDate = LocalDate.of(2025, 1, 1);
            LocalDate endDate = LocalDate.of(2025, 1, 31);
            List<BankTransactionJpaEntity> expectedList = List.of();

            when(bankTransactionJpaDao.findAllByCardIdAndDateBetween(cardId, startDate, endDate))
                    .thenReturn(expectedList);

            List<BankTransaction> actual = bankTransactionRepositoryImpl
                    .findAllByCardIdAndDateBetween(cardId, startDate, endDate);

            assertAll(
                    () -> assertEquals(0, actual.size()),
                    () -> assertTrue(actual.isEmpty()));
        }
    }

    @Nested
    class Save {
        @Test
        @DisplayName("save should persist and return transaction")
        void testSave() {
            when(bankTransactionJpaDao.save(any(BankTransactionJpaEntity.class)))
                    .thenReturn(transactionJpaEntity1);

            BankTransaction transactionToSave = new BankTransaction(
                    null,
                    BankTransactionType.Credit,
                    OriginBankingMovement.Transfer,
                    null,
                    LocalDateTime.of(2026, 1, 15, 10, 30, 0),
                    new BigDecimal("150.50"),
                    "Salary deposit",
                    null
            );

            BankTransaction actual = bankTransactionRepositoryImpl.save(transactionToSave);

            assertAll(
                    () -> assertNotNull(actual),
                    () -> assertEquals(1L, actual.getTransactionId()),
                    () -> assertEquals(BankTransactionType.Credit, actual.getType()),
                    () -> assertEquals(OriginBankingMovement.Transfer, actual.getOrigin()),
                    () -> assertEquals(0, new BigDecimal("150.50").compareTo(actual.getAmount())),
                    () -> assertEquals("Salary deposit", actual.getDescription()));
        }

        @Test
        @DisplayName("save should persist new transaction with generated id")
        void testSaveNewTransaction() {
            BankTransactionJpaEntity savedTransactionJpaEntity = new BankTransactionJpaEntity();
            savedTransactionJpaEntity.setTransactionId(3L);
            savedTransactionJpaEntity.setDate(LocalDateTime.of(2026, 1, 20, 9, 0, 0));
            savedTransactionJpaEntity.setAmount(new BigDecimal("200.00"));
            savedTransactionJpaEntity.setDescription("New transaction");
            savedTransactionJpaEntity.setTransactionTypeId(2L);
            savedTransactionJpaEntity.setTransactionOriginId(1L);
            savedTransactionJpaEntity.setCreditCard(creditCardJpaEntity);
            savedTransactionJpaEntity.setBankAccount(bankAccountJpaEntity);

            when(bankTransactionJpaDao.save(any(BankTransactionJpaEntity.class)))
                    .thenReturn(savedTransactionJpaEntity);

            BankTransaction transactionToSave = new BankTransaction(
                    null,
                    BankTransactionType.Credit,
                    OriginBankingMovement.Transfer,
                    null,
                    LocalDateTime.of(2026, 1, 20, 9, 0, 0),
                    new BigDecimal("200.00"),
                    "New transaction",
                    null
            );

            BankTransaction actual = bankTransactionRepositoryImpl.save(transactionToSave);

            assertAll(
                    () -> assertNotNull(actual),
                    () -> assertEquals(3L, actual.getTransactionId()),
                    () -> assertEquals(BankTransactionType.Credit, actual.getType()),
                    () -> assertEquals(OriginBankingMovement.Transfer, actual.getOrigin()),
                    () -> assertEquals(LocalDateTime.of(2026, 1, 20, 9, 0, 0), actual.getDate()),
                    () -> assertEquals(0, new BigDecimal("200.00").compareTo(actual.getAmount())),
                    () -> assertEquals("New transaction", actual.getDescription()));
        }
    }
}