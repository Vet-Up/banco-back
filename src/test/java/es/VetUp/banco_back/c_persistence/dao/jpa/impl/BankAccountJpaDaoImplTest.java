package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import es.VetUp.banco_back.c_persistence.TestConfig;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankAccountJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
class BankAccountJpaDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BankAccountJpaDao bankAccountJpaDao;

    private UserJpaEntity createAndPersistUser(String suffix) {
        UserJpaEntity user = new UserJpaEntity();
        user.setUsername("testuser_ba_" + suffix);
        user.setPassword("password123");
        user.setName("Test");
        user.setFirstSurname("User");
        user.setSecondSurname("BA");
        user.setDni("BA00" + suffix + "A");
        user.setApiKey("apikey_ba_" + suffix);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    private BankAccountJpaEntity createAndPersistBankAccount(UserJpaEntity user, String iban, BigDecimal balance) {
        BankAccountJpaEntity account = new BankAccountJpaEntity();
        account.setIban(iban);
        account.setBalance(balance);
        account.setUser(user);
        entityManager.persist(account);
        entityManager.flush();
        return account;
    }

    @Nested
    class GetAllByUserIdTests {

        @Test
        @DisplayName("getAllByUserId should return accounts for user")
        void testGetAllByUserId() {
            UserJpaEntity user = createAndPersistUser("001");
            createAndPersistBankAccount(user, "ES0000000000000000BA0001", new BigDecimal("1000.00"));
            createAndPersistBankAccount(user, "ES0000000000000000BA0002", new BigDecimal("2000.00"));

            List<BankAccountJpaEntity> result = bankAccountJpaDao.getAllByUserId(user.getUserId());

            assertAll("result",
                    () -> assertEquals(2, result.size()),
                    () -> assertTrue(result.stream().anyMatch(a -> "ES0000000000000000BA0001".equals(a.getIban()))),
                    () -> assertTrue(result.stream().anyMatch(a -> "ES0000000000000000BA0002".equals(a.getIban())))
            );
        }

        @Test
        @DisplayName("getAllByUserId should return empty list for unknown user")
        void testGetAllByUserIdNotFound() {
            List<BankAccountJpaEntity> result = bankAccountJpaDao.getAllByUserId(999999L);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class GetByIbanTests {

        @Test
        @DisplayName("getByIban should return account when found")
        void testGetByIban() {
            UserJpaEntity user = createAndPersistUser("002");
            createAndPersistBankAccount(user, "ES0000000000000000BA0003", new BigDecimal("3000.00"));

            Optional<BankAccountJpaEntity> result = bankAccountJpaDao.getByIban("ES0000000000000000BA0003");

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals("ES0000000000000000BA0003", result.get().getIban()),
                    () -> assertEquals(new BigDecimal("3000.00"), result.get().getBalance())
            );
        }

        @Test
        @DisplayName("getByIban should return empty when not found")
        void testGetByIbanNotFound() {
            Optional<BankAccountJpaEntity> result = bankAccountJpaDao.getByIban("ES9999999999999999999999");

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class GetByIdTests {

        @Test
        @DisplayName("getById should return account when found")
        void testGetById() {
            UserJpaEntity user = createAndPersistUser("003");
            BankAccountJpaEntity account = createAndPersistBankAccount(user, "ES0000000000000000BA0004", new BigDecimal("4000.00"));

            Optional<BankAccountJpaEntity> result = bankAccountJpaDao.getById(account.getAccountId());

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(account.getAccountId(), result.get().getAccountId()),
                    () -> assertEquals("ES0000000000000000BA0004", result.get().getIban())
            );
        }

        @Test
        @DisplayName("getById should return empty when not found")
        void testGetByIdNotFound() {
            Optional<BankAccountJpaEntity> result = bankAccountJpaDao.getById(999999L);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class SaveTests {

        @Test
        @DisplayName("save should persist new account")
        void testSaveNew() {
            UserJpaEntity user = createAndPersistUser("004");
            BankAccountJpaEntity account = new BankAccountJpaEntity();
            account.setIban("ES0000000000000000BA0005");
            account.setBalance(new BigDecimal("5000.00"));
            account.setUser(user);

            BankAccountJpaEntity saved = bankAccountJpaDao.save(account);

            assertAll("saved",
                    () -> assertNotNull(saved.getAccountId()),
                    () -> assertEquals("ES0000000000000000BA0005", saved.getIban()),
                    () -> assertEquals(new BigDecimal("5000.00"), saved.getBalance())
            );
        }

        @Test
        @DisplayName("save should update existing account")
        void testSaveUpdate() {
            UserJpaEntity user = createAndPersistUser("005");
            BankAccountJpaEntity account = createAndPersistBankAccount(user, "ES0000000000000000BA0006", new BigDecimal("6000.00"));
            entityManager.clear();

            account.setBalance(new BigDecimal("7000.00"));
            BankAccountJpaEntity updated = bankAccountJpaDao.save(account);
            entityManager.flush();
            entityManager.clear();

            Optional<BankAccountJpaEntity> found = bankAccountJpaDao.getById(account.getAccountId());
            assertAll("updated",
                    () -> assertTrue(found.isPresent()),
                    () -> assertEquals(new BigDecimal("7000.00"), found.get().getBalance())
            );
        }
    }
}
