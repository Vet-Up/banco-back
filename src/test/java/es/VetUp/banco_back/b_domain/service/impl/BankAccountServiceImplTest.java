package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private BankAccount testAccount1;
    private BankAccount testAccount2;

    @BeforeEach
    void setUp() {
        testAccount1 = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1000.00"), 1L);
        testAccount2 = new BankAccount(2L, "ES7620770024003102575766", new BigDecimal("2500.50"), 1L);
    }

    @Nested
    class GetAllByUserIdTests {

        @Test
        @DisplayName("getAllByUserId should return list of accounts for user")
        void testGetAllByUserId() {
            Long userId = 1L;
            when(bankAccountRepository.getAllByUserId(userId)).thenReturn(List.of(testAccount1, testAccount2));

            List<BankAccount> result = bankAccountService.getAllByUserId(userId);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(2, result.size()),
                    () -> assertEquals(1L, result.get(0).getAccountId()),
                    () -> assertEquals(2L, result.get(1).getAccountId())
            );
        }

        @Test
        @DisplayName("getAllByUserId should return empty list when no accounts found")
        void testGetAllByUserIdEmpty() {
            Long userId = 99L;
            when(bankAccountRepository.getAllByUserId(userId)).thenReturn(List.of());

            List<BankAccount> result = bankAccountService.getAllByUserId(userId);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertTrue(result.isEmpty())
            );
        }
    }

    @Nested
    class GetByIbanTests {

        @Test
        @DisplayName("getByIban should return account when found")
        void testGetByIban() {
            String iban = "ES9121000418450200051332";
            when(bankAccountRepository.getByIban(iban)).thenReturn(Optional.of(testAccount1));

            Optional<BankAccount> result = bankAccountService.getByIban(iban);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(testAccount1.getAccountId(), result.get().getAccountId()),
                    () -> assertEquals(iban, result.get().getIban())
            );
        }

        @Test
        @DisplayName("getByIban should return empty when not found")
        void testGetByIbanNotFound() {
            String iban = "ES0000000000000000000000";
            when(bankAccountRepository.getByIban(iban)).thenReturn(Optional.empty());

            Optional<BankAccount> result = bankAccountService.getByIban(iban);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class GetByIdTests {

        @Test
        @DisplayName("getById should return account when found")
        void testGetById() {
            Long accountId = 1L;
            when(bankAccountRepository.getById(accountId)).thenReturn(Optional.of(testAccount1));

            Optional<BankAccount> result = bankAccountService.getById(accountId);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(accountId, result.get().getAccountId())
            );
        }

        @Test
        @DisplayName("getById should return empty when not found")
        void testGetByIdNotFound() {
            Long accountId = 99L;
            when(bankAccountRepository.getById(accountId)).thenReturn(Optional.empty());

            Optional<BankAccount> result = bankAccountService.getById(accountId);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class SaveTests {

        @Test
        @DisplayName("save should return saved account")
        void testSave() {
            BankAccount newAccount = new BankAccount(null, "ES1234567890123456789012", new BigDecimal("500.00"), 1L);
            BankAccount savedAccount = new BankAccount(3L, "ES1234567890123456789012", new BigDecimal("500.00"), 1L);

            when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);

            BankAccount result = bankAccountService.save(newAccount);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(3L, result.getAccountId()),
                    () -> assertEquals(newAccount.getIban(), result.getIban())
            );
        }

        @Test
        @DisplayName("save should update existing account")
        void testSaveUpdate() {
            BankAccount updatedAccount = new BankAccount(1L, "ES9121000418450200051332", new BigDecimal("1500.00"), 1L);

            when(bankAccountRepository.save(updatedAccount)).thenReturn(updatedAccount);

            BankAccount result = bankAccountService.save(updatedAccount);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(1L, result.getAccountId()),
                    () -> assertEquals(0, new BigDecimal("1500.00").compareTo(result.getBalance()))
            );
        }
    }

    @Nested
    class FindByUserIdTests {

        @Test
        @DisplayName("findByUserId should return list of accounts")
        void testFindByUserId() {
            Long userId = 1L;
            when(bankAccountRepository.findByUserId(userId)).thenReturn(List.of(testAccount1, testAccount2));

            List<BankAccount> result = bankAccountService.findByUserId(userId);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(2, result.size())
            );
        }
    }
}
