package es.VetUp.banco_back.b_domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

    @Test
    @DisplayName("Test CreditCard Creation")
    void testCreditCardCreation() {
        Long sourceCardId = 1L;
        String cardNumber = "4111111111111111";
        String expirationDate = "2027-12-31";
        String cvv = "123";
        String fullName = "John Doe Smith";
        Long accountId = 1L;

        CreditCard creditCard = assertDoesNotThrow(() -> new CreditCard(
                sourceCardId, cardNumber, expirationDate, cvv, fullName, accountId));

        assertAll("creditCard",
                () -> assertEquals(sourceCardId, creditCard.getSourceCardId()),
                () -> assertEquals(cardNumber, creditCard.getCardNumber()),
                () -> assertEquals(expirationDate, creditCard.getExpirationDate()),
                () -> assertEquals(cvv, creditCard.getCvv()),
                () -> assertEquals(fullName, creditCard.getFullName()),
                () -> assertEquals(accountId, creditCard.getAccountId())
        );
    }

    @Test
    @DisplayName("Test CreditCard Creation with different card type")
    void testCreditCardCreationDifferentType() {
        Long sourceCardId = 2L;
        String cardNumber = "5500000000000004";
        String expirationDate = "2028-06-30";
        String cvv = "456";
        String fullName = "Jane Smith Roberts";
        Long accountId = 2L;

        CreditCard creditCard = assertDoesNotThrow(() -> new CreditCard(
                sourceCardId, cardNumber, expirationDate, cvv, fullName, accountId));

        assertAll("creditCard",
                () -> assertEquals(sourceCardId, creditCard.getSourceCardId()),
                () -> assertEquals(cardNumber, creditCard.getCardNumber()),
                () -> assertEquals(expirationDate, creditCard.getExpirationDate()),
                () -> assertEquals(cvv, creditCard.getCvv()),
                () -> assertEquals(fullName, creditCard.getFullName()),
                () -> assertEquals(accountId, creditCard.getAccountId())
        );
    }


    @Test
    @DisplayName("Test CreditCard Equals - Equal objects")
    void testCreditCardEqualsEqualObjects() {
        CreditCard creditCard1 = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "John Doe", 1L);
        CreditCard creditCard2 = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "John Doe", 1L);

        assertEquals(creditCard1, creditCard2);
    }

    @Test
    @DisplayName("Test CreditCard Equals - Different objects")
    void testCreditCardEqualsDifferentObjects() {
        CreditCard creditCard1 = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "John Doe", 1L);
        CreditCard creditCard2 = new CreditCard(2L, "5500000000000004", "2028-06-30", "456", "Jane Smith", 2L);

        assertNotEquals(creditCard1, creditCard2);
    }

    @Test
    @DisplayName("Test CreditCard Equals - Null comparison")
    void testCreditCardEqualsNull() {
        CreditCard creditCard = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "John Doe", 1L);

        assertNotEquals(null, creditCard);
    }

    
}
