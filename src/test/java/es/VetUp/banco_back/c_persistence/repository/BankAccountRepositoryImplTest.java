package es.VetUp.banco_back.c_persistence.repository;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankAccountJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountRepositoryImplTest {

    @Mock
    private BankAccountJpaDao bankAccountJpaDao;

    private BankAccountRepositoryImpl bankAccountRepository;

    private BankAccountJpaEntity testJpaEntity;

    private UserJpaEntity createUserWithId(Long userId) {
        UserJpaEntity user = new UserJpaEntity();
        user.setUserId(userId);
        return user;
    }

    @BeforeEach
    void setUp() {
        bankAccountRepository = new BankAccountRepositoryImpl(bankAccountJpaDao);
        testJpaEntity = new BankAccountJpaEntity();
        testJpaEntity.setAccountId(1L);
        testJpaEntity.setIban("ES1234567890123456789012");
        testJpaEntity.setBalance(new BigDecimal("1000.00"));
        testJpaEntity.setUser(createUserWithId(100L));
    }
    
    @Nested
    class GetAllByUserIdTests {

        @Test
        @DisplayName("getAllByUserId should return list of accounts")
        void testGetAllByUserId() {
            Long userId = 100L;
            when(bankAccountJpaDao.getAllByUserId(userId)).thenReturn(List.of(testJpaEntity));

            List<BankAccount> result = bankAccountRepository.getAllByUserId(userId);

            assertAll("result",
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals(testJpaEntity.getAccountId(), result.getFirst().getAccountId()),
                    () -> assertEquals(testJpaEntity.getIban(), result.getFirst().getIban())
            );
        }

        @Test
        @DisplayName("getAllByUserId should return empty list when no accounts")
        void testGetAllByUserIdEmpty() {
            Long userId = 999L;
            when(bankAccountJpaDao.getAllByUserId(userId)).thenReturn(Collections.emptyList());

            List<BankAccount> result = bankAccountRepository.getAllByUserId(userId);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class GetByIbanTests {

        @Test
        @DisplayName("getByIban should return account when found")
        void testGetByIban() {
            String iban = "ES1234567890123456789012";
            when(bankAccountJpaDao.getByIban(iban)).thenReturn(Optional.of(testJpaEntity));

            Optional<BankAccount> result = bankAccountRepository.getByIban(iban);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testJpaEntity.getIban(), result.get().getIban()),
                    () -> assertEquals(testJpaEntity.getBalance(), result.get().getBalance())
            );
        }

        @Test
        @DisplayName("getByIban should return empty when not found")
        void testGetByIbanNotFound() {
            String iban = "ES0000000000000000000000";
            when(bankAccountJpaDao.getByIban(iban)).thenReturn(Optional.empty());

            Optional<BankAccount> result = bankAccountRepository.getByIban(iban);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class GetByIdTests {

        @Test
        @DisplayName("getById should return account when found")
        void testGetById() {
            Long accountId = 1L;
            when(bankAccountJpaDao.getById(accountId)).thenReturn(Optional.of(testJpaEntity));

            Optional<BankAccount> result = bankAccountRepository.getById(accountId);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testJpaEntity.getAccountId(), result.get().getAccountId()),
                    () -> assertEquals(testJpaEntity.getIban(), result.get().getIban())
            );
        }

        @Test
        @DisplayName("getById should return empty when not found")
        void testGetByIdNotFound() {
            Long accountId = 999L;
            when(bankAccountJpaDao.getById(accountId)).thenReturn(Optional.empty());

            Optional<BankAccount> result = bankAccountRepository.getById(accountId);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class SaveTests {

        @Test
        @DisplayName("save should persist and return account")
        void testSave() {
            BankAccount accountToSave = new BankAccount(null, "ES9999999999999999999999", new BigDecimal("500.00"), 100L);
            BankAccountJpaEntity savedJpaEntity = new BankAccountJpaEntity();
            savedJpaEntity.setAccountId(2L);
            savedJpaEntity.setIban("ES9999999999999999999999");
            savedJpaEntity.setBalance(new BigDecimal("500.00"));
            savedJpaEntity.setUser(createUserWithId(100L));
            when(bankAccountJpaDao.save(any(BankAccountJpaEntity.class))).thenReturn(savedJpaEntity);

            BankAccount result = bankAccountRepository.save(accountToSave);

            assertAll("result",
                    () -> assertEquals(2L, result.getAccountId()),
                    () -> assertEquals("ES9999999999999999999999", result.getIban()),
                    () -> assertEquals(new BigDecimal("500.00"), result.getBalance())
            );
        }
    }

    @Nested
    class FindByUserIdTests {

        @Test
        @DisplayName("findByUserId should delegate to getAllByUserId")
        void testFindByUserId() {
            Long userId = 100L;
            when(bankAccountJpaDao.getAllByUserId(userId)).thenReturn(List.of(testJpaEntity));

            List<BankAccount> result = bankAccountRepository.findByUserId(userId);

            assertAll("result",
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals(testJpaEntity.getAccountId(), result.getFirst().getAccountId())
            );
        }
    }
}
