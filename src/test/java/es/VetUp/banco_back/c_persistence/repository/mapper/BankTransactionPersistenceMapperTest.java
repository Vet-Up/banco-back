package es.VetUp.banco_back.c_persistence.repository.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankTransactionJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BankTransactionPersistenceMapperTest {

    @Test
    @DisplayName("Test map from BankTransactionJpaEntity to BankTransaction")
    void testFromBankTransactionJpaEntityToBankTransaction() {
        // Arrange
        BankAccountJpaEntity bankAccountJpaEntity = new BankAccountJpaEntity();
        bankAccountJpaEntity.setAccountId(1L);
        bankAccountJpaEntity.setIban("ES9121000418450200051332");
        bankAccountJpaEntity.setBalance(new BigDecimal("1000.00"));

        CreditCardJpaEntity creditCardJpaEntity = new CreditCardJpaEntity();
        creditCardJpaEntity.setSourceCardId(1L);
        creditCardJpaEntity.setCardNumber("4111111111111111");
        creditCardJpaEntity.setExpirationDate(LocalDate.of(2028, 12, 31));
        creditCardJpaEntity.setCvv("123");
        creditCardJpaEntity.setFullName("John Doe");
        creditCardJpaEntity.setBankAccount(bankAccountJpaEntity);

        BankTransactionJpaEntity jpaEntity = new BankTransactionJpaEntity();
        jpaEntity.setTransactionId(1L);
        jpaEntity.setDate(LocalDateTime.of(2026, 1, 15, 10, 30, 0));
        jpaEntity.setAmount(new BigDecimal("150.50"));
        jpaEntity.setDescription("Salary deposit");
        jpaEntity.setTransactionTypeId(2L); // Credit
        jpaEntity.setTransactionOriginId(1L); // Transfer
        jpaEntity.setCreditCard(creditCardJpaEntity);
        jpaEntity.setBankAccount(bankAccountJpaEntity);

        // Act
        BankTransaction transaction = BankTransactionPersistenceMapper
                .fromBankTransactionJpaEntityToBankTransaction(jpaEntity);

        // Assert
        assertEquals(jpaEntity.getTransactionId(), transaction.getTransactionId());
        assertEquals(BankTransactionType.Credit, transaction.getType());
        assertEquals(OriginBankingMovement.Transfer, transaction.getOrigin());
        assertEquals(jpaEntity.getDate(), transaction.getDate());
        assertEquals(0, jpaEntity.getAmount().compareTo(transaction.getAmount()));
        assertEquals(jpaEntity.getDescription(), transaction.getDescription());
        assertNotNull(transaction.getCreditCard());
        assertNotNull(transaction.getBankAccount());
    }

    @Test
    @DisplayName("Test map from BankTransactionJpaEntity to BankTransaction with Debit type")
    void testFromBankTransactionJpaEntityToBankTransactionDebit() {
        // Arrange
        BankAccountJpaEntity bankAccountJpaEntity = new BankAccountJpaEntity();
        bankAccountJpaEntity.setAccountId(1L);
        bankAccountJpaEntity.setIban("ES9121000418450200051332");
        bankAccountJpaEntity.setBalance(new BigDecimal("1000.00"));

        BankTransactionJpaEntity jpaEntity = new BankTransactionJpaEntity();
        jpaEntity.setTransactionId(2L);
        jpaEntity.setDate(LocalDateTime.of(2026, 1, 10, 14, 0, 0));
        jpaEntity.setAmount(new BigDecimal("75.25"));
        jpaEntity.setDescription("Online purchase");
        jpaEntity.setTransactionTypeId(1L); // Debit
        jpaEntity.setTransactionOriginId(3L); // BankCard
        jpaEntity.setBankAccount(bankAccountJpaEntity);

        // Act
        BankTransaction transaction = BankTransactionPersistenceMapper
                .fromBankTransactionJpaEntityToBankTransaction(jpaEntity);

        // Assert
        assertEquals(jpaEntity.getTransactionId(), transaction.getTransactionId());
        assertEquals(BankTransactionType.Debit, transaction.getType());
        assertEquals(OriginBankingMovement.BankCard, transaction.getOrigin());
        assertEquals(jpaEntity.getDate(), transaction.getDate());
        assertEquals(0, jpaEntity.getAmount().compareTo(transaction.getAmount()));
        assertEquals(jpaEntity.getDescription(), transaction.getDescription());
    }

    @Test
    @DisplayName("Test map from BankTransactionJpaEntity to BankTransaction returns null when input is null")
    void testFromBankTransactionJpaEntityToBankTransactionNull() {
        // Act
        BankTransaction transaction = BankTransactionPersistenceMapper
                .fromBankTransactionJpaEntityToBankTransaction(null);

        // Assert
        assertNull(transaction);
    }

    @Test
    @DisplayName("Test map from BankTransaction to BankTransactionJpaEntity")
    void testFromBankTransactionToBankTransactionJpaEntity() {
        // Arrange
        BankAccount bankAccount = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1000.00"), 1L);
        CreditCard creditCard = new CreditCard(1L, "4111111111111111", "2028-12-31", "123", "John Doe", 1L);

        BankTransaction transaction = new BankTransaction(
                1L,
                BankTransactionType.Credit,
                OriginBankingMovement.Transfer,
                creditCard,
                LocalDateTime.of(2026, 1, 15, 10, 30, 0),
                new BigDecimal("150.50"),
                "Salary deposit",
                bankAccount
        );

        // Act
        BankTransactionJpaEntity jpaEntity = BankTransactionPersistenceMapper
                .fromBankTransactionToBankTransactionJpaEntity(transaction);

        // Assert
        assertEquals(transaction.getTransactionId(), jpaEntity.getTransactionId());
        assertEquals(2L, jpaEntity.getTransactionTypeId()); // Credit = 2
        assertEquals(1L, jpaEntity.getTransactionOriginId()); // Transfer = 1
        assertEquals(transaction.getDate(), jpaEntity.getDate());
        assertEquals(0, transaction.getAmount().compareTo(jpaEntity.getAmount()));
        assertEquals(transaction.getDescription(), jpaEntity.getDescription());
        assertNotNull(jpaEntity.getCreditCard());
        assertNotNull(jpaEntity.getBankAccount());
    }

    @Test
    @DisplayName("Test map from BankTransaction to BankTransactionJpaEntity with Debit type")
    void testFromBankTransactionToBankTransactionJpaEntityDebit() {
        // Arrange
        BankAccount bankAccount = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1000.00"), 1L);

        BankTransaction transaction = new BankTransaction(
                2L,
                BankTransactionType.Debit,
                OriginBankingMovement.DirectDebit,
                null,
                LocalDateTime.of(2026, 1, 10, 14, 0, 0),
                new BigDecimal("99.99"),
                "Monthly subscription",
                bankAccount
        );

        // Act
        BankTransactionJpaEntity jpaEntity = BankTransactionPersistenceMapper
                .fromBankTransactionToBankTransactionJpaEntity(transaction);

        // Assert
        assertEquals(transaction.getTransactionId(), jpaEntity.getTransactionId());
        assertEquals(1L, jpaEntity.getTransactionTypeId()); // Debit = 1
        assertEquals(2L, jpaEntity.getTransactionOriginId()); // DirectDebit = 2
        assertEquals(transaction.getDate(), jpaEntity.getDate());
        assertEquals(0, transaction.getAmount().compareTo(jpaEntity.getAmount()));
        assertEquals(transaction.getDescription(), jpaEntity.getDescription());
    }

    @Test
    @DisplayName("Test map from BankTransaction to BankTransactionJpaEntity returns null when input is null")
    void testFromBankTransactionToBankTransactionJpaEntityNull() {
        // Act
        BankTransactionJpaEntity jpaEntity = BankTransactionPersistenceMapper
                .fromBankTransactionToBankTransactionJpaEntity(null);

        // Assert
        assertNull(jpaEntity);
    }

}