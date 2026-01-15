package es.VetUp.banco_back.c_persistence.repository.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreditCardPersistenceMapperTest {

    @Test
    @DisplayName("Test map from CreditCardJpaEntity to CreditCard")
    void testFromCreditCardJpaEntityToCreditCard() {
        // Arrange
        BankAccountJpaEntity bankAccountJpaEntity = new BankAccountJpaEntity();
        bankAccountJpaEntity.setAccountId(1L);
        bankAccountJpaEntity.setIban("ES9121000418450200051332");
        bankAccountJpaEntity.setBalance(new BigDecimal("1000.00"));

        CreditCardJpaEntity jpaEntity = new CreditCardJpaEntity();
        jpaEntity.setSourceCardId(1L);
        jpaEntity.setCardNumber("4111111111111111");
        jpaEntity.setExpirationDate(LocalDate.of(2027, 12, 31));
        jpaEntity.setCvv("123");
        jpaEntity.setFullName("John Doe Smith");
        jpaEntity.setBankAccount(bankAccountJpaEntity);

        // Act
        CreditCard creditCard = CreditCardPersistenceMapper
                .fromCreditCardJpaEntityToCreditCard(jpaEntity);

        // Assert
        assertEquals(jpaEntity.getSourceCardId(), creditCard.getSourceCardId());
        assertEquals(jpaEntity.getCardNumber(), creditCard.getCardNumber());
        assertEquals("2027-12-31", creditCard.getExpirationDate());
        assertEquals(jpaEntity.getCvv(), creditCard.getCvv());
        assertEquals(jpaEntity.getFullName(), creditCard.getFullName());
        assertEquals(jpaEntity.getBankAccount().getAccountId(), creditCard.getAccountId());
    }

    @Test
    @DisplayName("Test map from CreditCardJpaEntity to CreditCard with different data")
    void testFromCreditCardJpaEntityToCreditCardDifferentData() {
        // Arrange
        BankAccountJpaEntity bankAccountJpaEntity = new BankAccountJpaEntity();
        bankAccountJpaEntity.setAccountId(2L);
        bankAccountJpaEntity.setIban("ES7620770024003102575766");
        bankAccountJpaEntity.setBalance(new BigDecimal("2500.00"));

        CreditCardJpaEntity jpaEntity = new CreditCardJpaEntity();
        jpaEntity.setSourceCardId(2L);
        jpaEntity.setCardNumber("5500000000000004");
        jpaEntity.setExpirationDate(LocalDate.of(2028, 6, 30));
        jpaEntity.setCvv("456");
        jpaEntity.setFullName("Jane Smith Roberts");
        jpaEntity.setBankAccount(bankAccountJpaEntity);

        // Act
        CreditCard creditCard = CreditCardPersistenceMapper
                .fromCreditCardJpaEntityToCreditCard(jpaEntity);

        // Assert
        assertEquals(jpaEntity.getSourceCardId(), creditCard.getSourceCardId());
        assertEquals(jpaEntity.getCardNumber(), creditCard.getCardNumber());
        assertEquals("2028-06-30", creditCard.getExpirationDate());
        assertEquals(jpaEntity.getCvv(), creditCard.getCvv());
        assertEquals(jpaEntity.getFullName(), creditCard.getFullName());
        assertEquals(jpaEntity.getBankAccount().getAccountId(), creditCard.getAccountId());
    }

    @Test
    @DisplayName("Test map from CreditCardJpaEntity to CreditCard returns null when input is null")
    void testFromCreditCardJpaEntityToCreditCardNull() {
        // Act
        CreditCard creditCard = CreditCardPersistenceMapper
                .fromCreditCardJpaEntityToCreditCard(null);

        // Assert
        assertNull(creditCard);
    }

    @Test
    @DisplayName("Test map from CreditCardJpaEntity to CreditCard with null BankAccount")
    void testFromCreditCardJpaEntityToCreditCardNullBankAccount() {
        // Arrange
        CreditCardJpaEntity jpaEntity = new CreditCardJpaEntity();
        jpaEntity.setSourceCardId(1L);
        jpaEntity.setCardNumber("4111111111111111");
        jpaEntity.setExpirationDate(LocalDate.of(2027, 12, 31));
        jpaEntity.setCvv("123");
        jpaEntity.setFullName("John Doe Smith");
        jpaEntity.setBankAccount(null);

        // Act
        CreditCard creditCard = CreditCardPersistenceMapper
                .fromCreditCardJpaEntityToCreditCard(jpaEntity);

        // Assert
        assertEquals(jpaEntity.getSourceCardId(), creditCard.getSourceCardId());
        assertEquals(jpaEntity.getCardNumber(), creditCard.getCardNumber());
        assertNull(creditCard.getAccountId());
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardJpaEntity")
    void testFromCreditCardToCreditCardJpaEntity() {
        // Arrange
        CreditCard creditCard = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "John Doe Smith", 1L);

        // Act
        CreditCardJpaEntity jpaEntity = CreditCardPersistenceMapper
                .fromCreditCardToCreditCardJpaEntity(creditCard);

        // Assert
        assertEquals(creditCard.getSourceCardId(), jpaEntity.getSourceCardId());
        assertEquals(creditCard.getCardNumber(), jpaEntity.getCardNumber());
        assertEquals(LocalDate.of(2027, 12, 31), jpaEntity.getExpirationDate());
        assertEquals(creditCard.getCvv(), jpaEntity.getCvv());
        assertEquals(creditCard.getFullName(), jpaEntity.getFullName());
        assertNotNull(jpaEntity.getBankAccount());
        assertEquals(creditCard.getAccountId(), jpaEntity.getBankAccount().getAccountId());
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardJpaEntity with different data")
    void testFromCreditCardToCreditCardJpaEntityDifferentData() {
        // Arrange
        CreditCard creditCard = new CreditCard(2L, "5500000000000004", "2028-06-30", "456", "Jane Smith Roberts", 2L);

        // Act
        CreditCardJpaEntity jpaEntity = CreditCardPersistenceMapper
                .fromCreditCardToCreditCardJpaEntity(creditCard);

        // Assert
        assertEquals(creditCard.getSourceCardId(), jpaEntity.getSourceCardId());
        assertEquals(creditCard.getCardNumber(), jpaEntity.getCardNumber());
        assertEquals(LocalDate.of(2028, 6, 30), jpaEntity.getExpirationDate());
        assertEquals(creditCard.getCvv(), jpaEntity.getCvv());
        assertEquals(creditCard.getFullName(), jpaEntity.getFullName());
        assertNotNull(jpaEntity.getBankAccount());
        assertEquals(creditCard.getAccountId(), jpaEntity.getBankAccount().getAccountId());
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardJpaEntity returns null when input is null")
    void testFromCreditCardToCreditCardJpaEntityNull() {
        // Act
        CreditCardJpaEntity jpaEntity = CreditCardPersistenceMapper
                .fromCreditCardToCreditCardJpaEntity(null);

        // Assert
        assertNull(jpaEntity);
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardJpaEntity with null accountId")
    void testFromCreditCardToCreditCardJpaEntityNullAccountId() {
        // Arrange
        CreditCard creditCard = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "John Doe Smith", null);

        // Act
        CreditCardJpaEntity jpaEntity = CreditCardPersistenceMapper
                .fromCreditCardToCreditCardJpaEntity(creditCard);

        // Assert
        assertEquals(creditCard.getSourceCardId(), jpaEntity.getSourceCardId());
        assertEquals(creditCard.getCardNumber(), jpaEntity.getCardNumber());
        assertNull(jpaEntity.getBankAccount());
    }
}
