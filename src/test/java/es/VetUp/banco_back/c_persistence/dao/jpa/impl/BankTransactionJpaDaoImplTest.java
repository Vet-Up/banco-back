package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import es.VetUp.banco_back.c_persistence.TestConfig;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankTransactionJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankTransactionJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@Import(TestConfig.class)
class BankTransactionJpaDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BankTransactionJpaDao bankTransactionJpaDao;

    @Test
    void testFindAll() {
        // Arrange: persiste un usuario, cuenta bancaria y transacción
        es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity user =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("One");
        user.setDni("TEST0001A");
        user.setApiKey("apikey_test_1");
        entityManager.persist(user);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity bankAccount =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity();
        bankAccount.setIban("ES9121000418450200051332");
        bankAccount.setBalance(new BigDecimal("1000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        BankTransactionJpaEntity entity = new BankTransactionJpaEntity();
        entity.setDate(LocalDateTime.of(2026, 1, 15, 10, 30, 0));
        entity.setAmount(new BigDecimal("150.50"));
        entity.setDescription("Test transaction");
        entity.setTransactionTypeId(2L);
        entity.setTransactionOriginId(1L);
        entity.setBankAccount(bankAccount);
        entityManager.persist(entity);
        entityManager.flush();

        // Act
        List<BankTransactionJpaEntity> result = bankTransactionJpaDao.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(t -> "Test transaction".equals(t.getDescription())));
    }

    @Test
    void testFindAllTransactionsByAccountId() {
        // Arrange: persiste un usuario, cuenta bancaria y transacción
        es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity user =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity();
        user.setUsername("testuser2");
        user.setPassword("password1232");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Two");
        user.setDni("TEST0002B");
        user.setApiKey("apikey_test_2");
        entityManager.persist(user);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity bankAccount =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000002");
        bankAccount.setBalance(new BigDecimal("2000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        BankTransactionJpaEntity entity = new BankTransactionJpaEntity();
        entity.setDate(LocalDateTime.of(2026, 1, 15, 11, 0, 0));
        entity.setAmount(new BigDecimal("150.50"));
        entity.setDescription("Account transaction");
        entity.setTransactionTypeId(2L);
        entity.setTransactionOriginId(1L);
        entity.setBankAccount(bankAccount);
        entityManager.persist(entity);
        entityManager.flush();

        // Act
        List<BankTransactionJpaEntity> result = bankTransactionJpaDao
                .findAllTransactionsByAccountId(bankAccount.getAccountId());

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(bankAccount.getAccountId(), result.get(0).getBankAccount().getAccountId());
    }

    @Test
    void testFindAllByCardId() {
        // Arrange: persiste un usuario, cuenta bancaria, tarjeta y transacción
        es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity user =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity();
        user.setUsername("testuser3");
        user.setPassword("password1233");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Three");
        user.setDni("TEST0003C");
        user.setApiKey("apikey_test_3");
        entityManager.persist(user);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity bankAccount =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity();
        bankAccount.setIban("ES1234567890123456789012");
        bankAccount.setBalance(new BigDecimal("3000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity creditCard =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity();
        creditCard.setCardNumber("9999111122223333");
        creditCard.setExpirationDate(LocalDate.of(2028, 12, 31));
        creditCard.setCvv("123");
        creditCard.setFullName("John Doe");
        creditCard.setBankAccount(bankAccount);
        entityManager.persist(creditCard);
        entityManager.flush();

        BankTransactionJpaEntity entity = new BankTransactionJpaEntity();
        entity.setDate(LocalDateTime.of(2026, 1, 15, 12, 0, 0));
        entity.setAmount(new BigDecimal("75.25"));
        entity.setDescription("Card transaction");
        entity.setTransactionTypeId(1L);
        entity.setTransactionOriginId(3L);
        entity.setCreditCard(creditCard);
        entity.setBankAccount(bankAccount);
        entityManager.persist(entity);
        entityManager.flush();

        // Act
        List<BankTransactionJpaEntity> result = bankTransactionJpaDao
                .findAllByCardId(creditCard.getSourceCardId());

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(creditCard.getSourceCardId(), result.get(0).getCreditCard().getSourceCardId());
    }

    @Test
    void testFindAllByCardIdAndDateBetween() {
        // Arrange: persiste un usuario, cuenta bancaria, tarjeta y transacción
        es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity user =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity();
        user.setUsername("testuser4");
        user.setPassword("password1234");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Four");
        user.setDni("TEST0004D");
        user.setApiKey("apikey_test_4");
        entityManager.persist(user);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity bankAccount =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity();
        bankAccount.setIban("ES9876543210987654321098");
        bankAccount.setBalance(new BigDecimal("4000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity creditCard =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity();
        creditCard.setCardNumber("9999444455556666");
        creditCard.setExpirationDate(LocalDate.of(2027, 6, 30));
        creditCard.setCvv("456");
        creditCard.setFullName("Jane Smith");
        creditCard.setBankAccount(bankAccount);
        entityManager.persist(creditCard);
        entityManager.flush();

        BankTransactionJpaEntity entity = new BankTransactionJpaEntity();
        entity.setDate(LocalDateTime.of(2026, 1, 15, 13, 0, 0));
        entity.setAmount(new BigDecimal("100.00"));
        entity.setDescription("Date range transaction");
        entity.setTransactionTypeId(2L);
        entity.setTransactionOriginId(1L);
        entity.setCreditCard(creditCard);
        entity.setBankAccount(bankAccount);
        entityManager.persist(entity);
        entityManager.flush();

        // Act
        List<BankTransactionJpaEntity> result = bankTransactionJpaDao
                .findAllByCardIdAndDateBetween(
                        creditCard.getSourceCardId(),
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 1, 31)
                );

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Date range transaction", result.get(0).getDescription());
    }

    @Test
    void testFindByTransactionId() {
        // Arrange: persiste un usuario, cuenta bancaria y transacción
        es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity user =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity();
        user.setUsername("testuser5");
        user.setPassword("password1235");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Five");
        user.setDni("TEST0005E");
        user.setApiKey("apikey_test_5");
        entityManager.persist(user);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity bankAccount =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity();
        bankAccount.setIban("ES1111222233334444555566");
        bankAccount.setBalance(new BigDecimal("5000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        BankTransactionJpaEntity entity = new BankTransactionJpaEntity();
        entity.setDate(LocalDateTime.of(2026, 1, 15, 14, 0, 0));
        entity.setAmount(new BigDecimal("200.00"));
        entity.setDescription("Find by ID transaction");
        entity.setTransactionTypeId(2L);
        entity.setTransactionOriginId(1L);
        entity.setBankAccount(bankAccount);
        entityManager.persist(entity);
        entityManager.flush();
        Long transactionId = entity.getTransactionId();

        // Act
        var result = bankTransactionJpaDao.findByTransactionId(transactionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Find by ID transaction", result.get().getDescription());
    }

    @Test
    void testSaveInsert() {
        // Arrange: persiste un usuario y cuenta bancaria
        es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity user =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity();
        user.setUsername("testuser6");
        user.setPassword("password1236");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Six");
        user.setDni("TEST0006F");
        user.setApiKey("apikey_test_6");
        entityManager.persist(user);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity bankAccount =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity();
        bankAccount.setIban("ES6666777788889999000011");
        bankAccount.setBalance(new BigDecimal("6000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        BankTransactionJpaEntity entity = new BankTransactionJpaEntity();
        entity.setDate(LocalDateTime.of(2026, 1, 20, 9, 0, 0));
        entity.setAmount(new BigDecimal("300.00"));
        entity.setDescription("New transaction");
        entity.setTransactionTypeId(2L);
        entity.setTransactionOriginId(1L);
        entity.setBankAccount(bankAccount);

        // Act
        BankTransactionJpaEntity savedEntity = bankTransactionJpaDao.save(entity);

        // Assert
        assertNotNull(savedEntity.getTransactionId());
        assertEquals("New transaction", savedEntity.getDescription());
    }

    @Test
    void testSaveUpdate() {
        // Arrange: persiste un usuario, cuenta bancaria y transacción
        es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity user =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity();
        user.setUsername("testuser7");
        user.setPassword("password1237");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Seven");
        user.setDni("TEST0007G");
        user.setApiKey("apikey_test_7");
        entityManager.persist(user);
        entityManager.flush();

        es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity bankAccount =
                new es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity();
        bankAccount.setIban("ES7777888899990000111122");
        bankAccount.setBalance(new BigDecimal("7000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        BankTransactionJpaEntity entity = new BankTransactionJpaEntity();
        entity.setDate(LocalDateTime.of(2026, 1, 15, 15, 0, 0));
        entity.setAmount(new BigDecimal("150.00"));
        entity.setDescription("Original description");
        entity.setTransactionTypeId(2L);
        entity.setTransactionOriginId(1L);
        entity.setBankAccount(bankAccount);
        entityManager.persist(entity);
        entityManager.flush();

        // Act
        entity.setDescription("Updated description");
        BankTransactionJpaEntity updatedEntity = bankTransactionJpaDao.save(entity);

        // Assert
        assertEquals("Updated description", updatedEntity.getDescription());
    }

}