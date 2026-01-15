package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.repository.CreditCardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImplTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardServiceImpl creditCardServiceImpl;

    private CreditCard createTestCreditCard(Long sourceCardId, Long accountId) {
        return new CreditCard(
                sourceCardId,
                "4111111111111111",
                "2027-12-31",
                "123",
                "John Doe Smith",
                accountId
        );
    }

    @Nested
    class GetCreditCardByIdTests {

        @Test
        @DisplayName("getCreditCardById should return credit card when found")
        void testGetCreditCardById() {
            Long sourceCardId = 1L;
            CreditCard creditCard = createTestCreditCard(sourceCardId, 1L);

            when(creditCardRepository.findById(sourceCardId)).thenReturn(Optional.of(creditCard));

            CreditCard result = creditCardServiceImpl.getCreditCardById(sourceCardId);

            assertAll("result",
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(sourceCardId, result.getSourceCardId()),
                    () -> assertEquals("4111111111111111", result.getCardNumber()),
                    () -> assertEquals("John Doe Smith", result.getFullName()));
        }

        @Test
        @DisplayName("getCreditCardById should throw exception when not found")
        void testGetCreditCardByIdNotFound() {
            Long sourceCardId = 99L;

            when(creditCardRepository.findById(sourceCardId)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> creditCardServiceImpl.getCreditCardById(sourceCardId));

            assertEquals("Credit card with id 99 not found", exception.getMessage());
        }
    }

    @Nested
    class FindByCardNumberTests {

        @Test
        @DisplayName("findByCardNumber should return credit card when found")
        void testFindByCardNumber() {
            String cardNumber = "4111111111111111";
            CreditCard creditCard = createTestCreditCard(1L, 1L);

            when(creditCardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.of(creditCard));

            Optional<CreditCard> result = creditCardServiceImpl.findByCardNumber(cardNumber);

            assertAll("result",
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(cardNumber, result.get().getCardNumber()));
        }

        @Test
        @DisplayName("findByCardNumber should return empty when not found")
        void testFindByCardNumberNotFound() {
            String cardNumber = "9999999999999999";

            when(creditCardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.empty());

            Optional<CreditCard> result = creditCardServiceImpl.findByCardNumber(cardNumber);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class GetAllCreditCardsByUserTests {

        @Test
        @DisplayName("getAllCreditCardsByUser should return list of credit cards")
        void testGetAllCreditCardsByUser() {
            Long userId = 1L;

            CreditCard creditCard1 = createTestCreditCard(1L, userId);
            CreditCard creditCard2 = new CreditCard(2L, "5500000000000004", "2028-06-30", "456", "Jane Smith", userId);

            List<CreditCard> creditCards = List.of(creditCard1, creditCard2);

            when(creditCardRepository.findAllByAccountId(userId)).thenReturn(creditCards);

            List<CreditCard> result = creditCardServiceImpl.getAllCreditCardsByUser(userId);

            assertAll("result",
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(2, result.size(), "Result size should be 2"),
                    () -> assertEquals(1L, result.get(0).getSourceCardId()),
                    () -> assertEquals(2L, result.get(1).getSourceCardId()));
        }

        @Test
        @DisplayName("getAllCreditCardsByUser should return empty list when no cards found")
        void testGetAllCreditCardsByUserEmpty() {
            Long userId = 99L;

            when(creditCardRepository.findAllByAccountId(userId)).thenReturn(List.of());

            List<CreditCard> result = creditCardServiceImpl.getAllCreditCardsByUser(userId);

            assertAll("result",
                    () -> assertNotNull(result),
                    () -> assertEquals(0, result.size()));
        }
    }

    @Nested
    class ValidateCreditCardDetailsTests {

        @Test
        @DisplayName("validateCreditCardDetails should return true when card is valid")
        void testValidateCreditCardDetailsValid() {
            CreditCard creditCard = createTestCreditCard(1L, 1L);

            when(creditCardRepository.validateCard(creditCard)).thenReturn(true);

            boolean result = creditCardServiceImpl.validateCreditCardDetails(creditCard);

            assertTrue(result);
        }

        @Test
        @DisplayName("validateCreditCardDetails should return false when card is invalid")
        void testValidateCreditCardDetailsInvalid() {
            CreditCard creditCard = createTestCreditCard(1L, 1L);

            when(creditCardRepository.validateCard(creditCard)).thenReturn(false);

            boolean result = creditCardServiceImpl.validateCreditCardDetails(creditCard);

            assertFalse(result);
        }
    }

    @Nested
    class IsExpiredTests {

        @Test
        @DisplayName("isExpired should return true when card is expired")
        void testIsExpiredTrue() {
            Long sourceCardId = 1L;

            when(creditCardRepository.isExpired(sourceCardId)).thenReturn(true);

            boolean result = creditCardServiceImpl.isExpired(sourceCardId);

            assertTrue(result);
        }

        @Test
        @DisplayName("isExpired should return false when card is not expired")
        void testIsExpiredFalse() {
            Long sourceCardId = 1L;

            when(creditCardRepository.isExpired(sourceCardId)).thenReturn(false);

            boolean result = creditCardServiceImpl.isExpired(sourceCardId);

            assertFalse(result);
        }
    }
}
