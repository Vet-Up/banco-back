package es.VetUp.banco_back.c_persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.c_persistence.dao.jpa.CreditCardJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
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
class CreditCardRepositoryImplTest {

    @Mock
    private CreditCardJpaDao creditCardJpaDao;

    @InjectMocks
    private CreditCardRepositoryImpl creditCardRepositoryImpl;

    CreditCardJpaEntity creditCardJpaEntity1;
    CreditCardJpaEntity creditCardJpaEntity2;
    BankAccountJpaEntity bankAccountJpaEntity;

    @BeforeEach
    void setUp() {
        bankAccountJpaEntity = new BankAccountJpaEntity();
        bankAccountJpaEntity.setAccountId(1L);
        bankAccountJpaEntity.setIban("ES9121000418450200051332");

        creditCardJpaEntity1 = new CreditCardJpaEntity();
        creditCardJpaEntity1.setSourceCardId(1L);
        creditCardJpaEntity1.setCardNumber("4111111111111111");
        creditCardJpaEntity1.setExpirationDate(LocalDate.of(2027, 12, 31));
        creditCardJpaEntity1.setCvv("123");
        creditCardJpaEntity1.setFullName("John Doe Smith");
        creditCardJpaEntity1.setBankAccount(bankAccountJpaEntity);

        creditCardJpaEntity2 = new CreditCardJpaEntity();
        creditCardJpaEntity2.setSourceCardId(2L);
        creditCardJpaEntity2.setCardNumber("5500000000000004");
        creditCardJpaEntity2.setExpirationDate(LocalDate.of(2028, 6, 30));
        creditCardJpaEntity2.setCvv("456");
        creditCardJpaEntity2.setFullName("Jane Smith Roberts");
        creditCardJpaEntity2.setBankAccount(bankAccountJpaEntity);
    }

    @Nested
    class FindById {
        @Test
        @DisplayName("findById should return credit card when found")
        void testFindById() {
            when(creditCardJpaDao.findById(1L)).thenReturn(Optional.of(creditCardJpaEntity1));

            Optional<CreditCard> actual = creditCardRepositoryImpl.findById(1L);

            assertAll(
                    () -> assertTrue(actual.isPresent()),
                    () -> assertEquals(1L, actual.get().getSourceCardId()),
                    () -> assertEquals("4111111111111111", actual.get().getCardNumber()),
                    () -> assertEquals("2027-12-31", actual.get().getExpirationDate()),
                    () -> assertEquals("123", actual.get().getCvv()),
                    () -> assertEquals("John Doe Smith", actual.get().getFullName()),
                    () -> assertEquals(1L, actual.get().getAccountId()));
        }

        @Test
        @DisplayName("findById should return empty when not found")
        void testFindByIdNotFound() {
            when(creditCardJpaDao.findById(99L)).thenReturn(Optional.empty());

            Optional<CreditCard> actual = creditCardRepositoryImpl.findById(99L);

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    class FindByCardNumber {
        @Test
        @DisplayName("findByCardNumber should return credit card when found")
        void testFindByCardNumber() {
            String cardNumber = "4111111111111111";
            when(creditCardJpaDao.findByCardNumber(cardNumber)).thenReturn(Optional.of(creditCardJpaEntity1));

            Optional<CreditCard> actual = creditCardRepositoryImpl.findByCardNumber(cardNumber);

            assertAll(
                    () -> assertTrue(actual.isPresent()),
                    () -> assertEquals(1L, actual.get().getSourceCardId()),
                    () -> assertEquals(cardNumber, actual.get().getCardNumber()),
                    () -> assertEquals("John Doe Smith", actual.get().getFullName()));
        }

        @Test
        @DisplayName("findByCardNumber should return empty when not found")
        void testFindByCardNumberNotFound() {
            String cardNumber = "9999999999999999";
            when(creditCardJpaDao.findByCardNumber(cardNumber)).thenReturn(Optional.empty());

            Optional<CreditCard> actual = creditCardRepositoryImpl.findByCardNumber(cardNumber);

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    class FindAllByAccountId {
        @Test
        @DisplayName("findAllByAccountId should return credit cards for given account")
        void testFindAllByAccountId() {
            Long accountId = 1L;
            List<CreditCardJpaEntity> expectedList = List.of(creditCardJpaEntity1, creditCardJpaEntity2);

            when(creditCardJpaDao.findAllByAccountId(accountId)).thenReturn(expectedList);

            List<CreditCard> actual = creditCardRepositoryImpl.findAllByAccountId(accountId);

            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(1L, actual.get(0).getSourceCardId()),
                    () -> assertEquals(2L, actual.get(1).getSourceCardId()),
                    () -> assertEquals("4111111111111111", actual.get(0).getCardNumber()),
                    () -> assertEquals("5500000000000004", actual.get(1).getCardNumber()));
        }

        @Test
        @DisplayName("findAllByAccountId should return empty list when no cards found")
        void testFindAllByAccountIdEmpty() {
            Long accountId = 99L;
            List<CreditCardJpaEntity> expectedList = List.of();

            when(creditCardJpaDao.findAllByAccountId(accountId)).thenReturn(expectedList);

            List<CreditCard> actual = creditCardRepositoryImpl.findAllByAccountId(accountId);

            assertAll(
                    () -> assertEquals(0, actual.size()),
                    () -> assertTrue(actual.isEmpty()));
        }
    }

    @Nested
    class ValidateCard {
        @Test
        @DisplayName("validateCard should return true when card is valid")
        void testValidateCardValid() {
            CreditCard creditCard = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "John Doe Smith", 1L);

            when(creditCardJpaDao.validateCreditCard(any(CreditCardJpaEntity.class))).thenReturn(true);

            Boolean actual = creditCardRepositoryImpl.validateCard(creditCard);

            assertTrue(actual);
        }

        @Test
        @DisplayName("validateCard should return false when card is invalid")
        void testValidateCardInvalid() {
            CreditCard creditCard = new CreditCard(1L, "4111111111111111", "2027-12-31", "999", "John Doe Smith", 1L);

            when(creditCardJpaDao.validateCreditCard(any(CreditCardJpaEntity.class))).thenReturn(false);

            Boolean actual = creditCardRepositoryImpl.validateCard(creditCard);

            assertFalse(actual);
        }
    }

    @Nested
    class IsExpired {
        @Test
        @DisplayName("isExpired should return true when card is expired")
        void testIsExpiredTrue() {
            Long sourceCardId = 1L;

            when(creditCardJpaDao.isExpired(sourceCardId)).thenReturn(true);

            Boolean actual = creditCardRepositoryImpl.isExpired(sourceCardId);

            assertTrue(actual);
        }

        @Test
        @DisplayName("isExpired should return false when card is not expired")
        void testIsExpiredFalse() {
            Long sourceCardId = 1L;

            when(creditCardJpaDao.isExpired(sourceCardId)).thenReturn(false);

            Boolean actual = creditCardRepositoryImpl.isExpired(sourceCardId);

            assertFalse(actual);
        }
    }

    @Nested
    class FindByAccountId {
        @Test
        @DisplayName("findByAccountId should return credit cards for given account")
        void testFindByAccountId() {
            Long accountId = 1L;
            List<CreditCardJpaEntity> expectedList = List.of(creditCardJpaEntity1, creditCardJpaEntity2);

            when(creditCardJpaDao.findByAccountId(accountId)).thenReturn(expectedList);

            List<CreditCard> actual = creditCardRepositoryImpl.findByAccountId(accountId);

            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(1L, actual.get(0).getSourceCardId()),
                    () -> assertEquals(2L, actual.get(1).getSourceCardId()),
                    () -> assertEquals("4111111111111111", actual.get(0).getCardNumber()),
                    () -> assertEquals("5500000000000004", actual.get(1).getCardNumber()));
        }

        @Test
        @DisplayName("findByAccountId should return empty list when no cards found")
        void testFindByAccountIdEmpty() {
            Long accountId = 99L;
            List<CreditCardJpaEntity> expectedList = List.of();

            when(creditCardJpaDao.findByAccountId(accountId)).thenReturn(expectedList);

            List<CreditCard> actual = creditCardRepositoryImpl.findByAccountId(accountId);

            assertAll(
                    () -> assertEquals(0, actual.size()),
                    () -> assertTrue(actual.isEmpty()));
        }
    }
}
