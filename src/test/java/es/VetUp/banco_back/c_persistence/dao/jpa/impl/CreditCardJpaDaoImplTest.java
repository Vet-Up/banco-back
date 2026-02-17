package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import es.VetUp.banco_back.c_persistence.TestConfig;
import es.VetUp.banco_back.c_persistence.dao.jpa.CreditCardJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@Import(TestConfig.class)
class CreditCardJpaDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CreditCardJpaDao creditCardJpaDao;

    @Test
    void testFindById() {
        // Arrange: persist user, bank account, and credit card
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_cc1");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("One");
        user.setDni("CCTEST01A");
        user.setApiKey("apikey_cc_1");
        entityManager.persist(user);
        entityManager.flush();

        BankAccountJpaEntity bankAccount = new BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000101");
        bankAccount.setBalance(new BigDecimal("1000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        CreditCardJpaEntity creditCard = new CreditCardJpaEntity();
        creditCard.setCardNumber("4111111111110001");
        creditCard.setExpirationDate(LocalDate.of(2027, 12, 31));
        creditCard.setCvv("123");
        creditCard.setFullName("John Doe Smith");
        creditCard.setBankAccount(bankAccount);
        entityManager.persist(creditCard);
        entityManager.flush();
        Long cardId = creditCard.getSourceCardId();

        // Act
        var result = creditCardJpaDao.findById(cardId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("4111111111110001", result.get().getCardNumber());
        assertEquals("John Doe Smith", result.get().getFullName());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        var result = creditCardJpaDao.findById(99999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByCardNumber() {
        // Arrange: persist user, bank account, and credit card
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_cc2");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Two");
        user.setDni("CCTEST02B");
        user.setApiKey("apikey_cc_2");
        entityManager.persist(user);
        entityManager.flush();

        BankAccountJpaEntity bankAccount = new BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000102");
        bankAccount.setBalance(new BigDecimal("2000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        CreditCardJpaEntity creditCard = new CreditCardJpaEntity();
        creditCard.setCardNumber("5500000000000002");
        creditCard.setExpirationDate(LocalDate.of(2028, 6, 30));
        creditCard.setCvv("456");
        creditCard.setFullName("Jane Smith Roberts");
        creditCard.setBankAccount(bankAccount);
        entityManager.persist(creditCard);
        entityManager.flush();

        // Act
        var result = creditCardJpaDao.findByCardNumber("5500000000000002");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Jane Smith Roberts", result.get().getFullName());
        assertEquals("456", result.get().getCvv());
    }

    @Test
    void testFindByCardNumberNotFound() {
        // Act
        var result = creditCardJpaDao.findByCardNumber("9999999999999999");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAllByAccountId() {
        // Arrange: persist user, bank account, and multiple credit cards
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_cc3");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Three");
        user.setDni("CCTEST03C");
        user.setApiKey("apikey_cc_3");
        entityManager.persist(user);
        entityManager.flush();

        BankAccountJpaEntity bankAccount = new BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000103");
        bankAccount.setBalance(new BigDecimal("3000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        CreditCardJpaEntity creditCard1 = new CreditCardJpaEntity();
        creditCard1.setCardNumber("4111111111110003");
        creditCard1.setExpirationDate(LocalDate.of(2027, 12, 31));
        creditCard1.setCvv("111");
        creditCard1.setFullName("Account Holder One");
        creditCard1.setBankAccount(bankAccount);
        entityManager.persist(creditCard1);

        CreditCardJpaEntity creditCard2 = new CreditCardJpaEntity();
        creditCard2.setCardNumber("5500000000000003");
        creditCard2.setExpirationDate(LocalDate.of(2028, 6, 30));
        creditCard2.setCvv("222");
        creditCard2.setFullName("Account Holder Two");
        creditCard2.setBankAccount(bankAccount);
        entityManager.persist(creditCard2);
        entityManager.flush();

        // Act
        List<CreditCardJpaEntity> result = creditCardJpaDao.findAllByAccountId(bankAccount.getAccountId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> "4111111111110003".equals(c.getCardNumber())));
        assertTrue(result.stream().anyMatch(c -> "5500000000000003".equals(c.getCardNumber())));
    }

    @Test
    void testFindAllByAccountIdEmpty() {
        // Act
        List<CreditCardJpaEntity> result = creditCardJpaDao.findAllByAccountId(99999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testValidateCreditCard() {
        // Arrange: persist user, bank account, and credit card
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_cc4");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Four");
        user.setDni("CCTEST04D");
        user.setApiKey("apikey_cc_4");
        entityManager.persist(user);
        entityManager.flush();

        BankAccountJpaEntity bankAccount = new BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000104");
        bankAccount.setBalance(new BigDecimal("4000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        CreditCardJpaEntity creditCard = new CreditCardJpaEntity();
        creditCard.setCardNumber("4111111111110004");
        creditCard.setExpirationDate(LocalDate.of(2027, 12, 31));
        creditCard.setCvv("333");
        creditCard.setFullName("Valid Card Holder");
        creditCard.setBankAccount(bankAccount);
        entityManager.persist(creditCard);
        entityManager.flush();

        // Create entity for validation with matching data
        CreditCardJpaEntity validationCard = new CreditCardJpaEntity();
        validationCard.setCardNumber("4111111111110004");
        validationCard.setExpirationDate(LocalDate.of(2027, 12, 31));
        validationCard.setCvv("333");

        // Act
        Boolean result = creditCardJpaDao.validateCreditCard(validationCard);

        // Assert
        assertTrue(result);
    }

    @Test
    void testValidateCreditCardInvalid() {
        // Arrange: create entity for validation with non-existing data
        CreditCardJpaEntity validationCard = new CreditCardJpaEntity();
        validationCard.setCardNumber("9999999999999999");
        validationCard.setExpirationDate(LocalDate.of(2027, 12, 31));
        validationCard.setCvv("999");

        // Act
        Boolean result = creditCardJpaDao.validateCreditCard(validationCard);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsExpiredFalse() {
        // Arrange: persist user, bank account, and credit card with future expiration
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_cc5");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Five");
        user.setDni("CCTEST05E");
        user.setApiKey("apikey_cc_5");
        entityManager.persist(user);
        entityManager.flush();

        BankAccountJpaEntity bankAccount = new BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000105");
        bankAccount.setBalance(new BigDecimal("5000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        CreditCardJpaEntity creditCard = new CreditCardJpaEntity();
        creditCard.setCardNumber("4111111111110005");
        creditCard.setExpirationDate(LocalDate.of(2030, 12, 31)); // Future date
        creditCard.setCvv("444");
        creditCard.setFullName("Not Expired Card Holder");
        creditCard.setBankAccount(bankAccount);
        entityManager.persist(creditCard);
        entityManager.flush();

        // Act
        Boolean result = creditCardJpaDao.isExpired(creditCard.getSourceCardId());

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsExpiredTrue() {
        // Arrange: persist user, bank account, and credit card with past expiration
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_cc6");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Six");
        user.setDni("CCTEST06F");
        user.setApiKey("apikey_cc_6");
        entityManager.persist(user);
        entityManager.flush();

        BankAccountJpaEntity bankAccount = new BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000106");
        bankAccount.setBalance(new BigDecimal("6000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        CreditCardJpaEntity creditCard = new CreditCardJpaEntity();
        creditCard.setCardNumber("4111111111110006");
        creditCard.setExpirationDate(LocalDate.of(2020, 1, 1)); // Past date
        creditCard.setCvv("555");
        creditCard.setFullName("Expired Card Holder");
        creditCard.setBankAccount(bankAccount);
        entityManager.persist(creditCard);
        entityManager.flush();

        // Act
        Boolean result = creditCardJpaDao.isExpired(creditCard.getSourceCardId());

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsExpiredNotFound() {
        // Act: check expiration for non-existing card
        Boolean result = creditCardJpaDao.isExpired(99999L);

        // Assert: should return true (considered expired if not found)
        assertTrue(result);
    }

    @Test
    void testFindByAccountId() {
        // Arrange: persist user, bank account, and multiple credit cards
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_cc7");
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("Seven");
        user.setDni("CCTEST07G");
        user.setApiKey("apikey_cc_7");
        entityManager.persist(user);
        entityManager.flush();

        BankAccountJpaEntity bankAccount = new BankAccountJpaEntity();
        bankAccount.setIban("ES0000000000000000000107");
        bankAccount.setBalance(new BigDecimal("7000.00"));
        bankAccount.setUser(user);
        entityManager.persist(bankAccount);
        entityManager.flush();

        CreditCardJpaEntity creditCard1 = new CreditCardJpaEntity();
        creditCard1.setCardNumber("4111111111110007");
        creditCard1.setExpirationDate(LocalDate.of(2027, 12, 31));
        creditCard1.setCvv("111");
        creditCard1.setFullName("Account Holder Seven");
        creditCard1.setBankAccount(bankAccount);
        entityManager.persist(creditCard1);

        CreditCardJpaEntity creditCard2 = new CreditCardJpaEntity();
        creditCard2.setCardNumber("5500000000000007");
        creditCard2.setExpirationDate(LocalDate.of(2028, 6, 30));
        creditCard2.setCvv("222");
        creditCard2.setFullName("Account Holder Eight");
        creditCard2.setBankAccount(bankAccount);
        entityManager.persist(creditCard2);
        entityManager.flush();

        // Act
        List<CreditCardJpaEntity> result = creditCardJpaDao.findByAccountId(bankAccount.getAccountId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> "4111111111110007".equals(c.getCardNumber())));
        assertTrue(result.stream().anyMatch(c -> "5500000000000007".equals(c.getCardNumber())));
    }

    @Test
    void testFindByAccountIdEmpty() {
        // Act
        List<CreditCardJpaEntity> result = creditCardJpaDao.findByAccountId(99999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
