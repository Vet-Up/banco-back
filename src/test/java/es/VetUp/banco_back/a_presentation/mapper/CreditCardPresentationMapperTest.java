package es.VetUp.banco_back.a_presentation.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.VetUp.banco_back.a_presentation.webModel.response.CreditCardResponse;
import es.VetUp.banco_back.a_presentation.webModel.response.summary.CreditCardSummaryResponse;
import es.VetUp.banco_back.b_domain.model.CreditCard;

class CreditCardPresentationMapperTest {

    @Test
    @DisplayName("Test map from CreditCard to CreditCardResponse")
    void testFromCreditCardToCreditCardResponse() {
        CreditCard creditCard = new CreditCard(
                1L,
                "4111111111111111",
                "2027-12-31",
                "123",
                "John Doe Smith",
                1L);

        CreditCardResponse response = CreditCardPresentationMapper
                .fromCreditCardToCreditCardResponse(creditCard);

        assertEquals(creditCard.getSourceCardId(), response.creditCardId());
        assertEquals(creditCard.getCardNumber(), response.cardNumber());
        assertEquals(creditCard.getExpirationDate(), response.expirationDate());
        assertEquals(creditCard.getCvv(), response.cvv());
        assertEquals(creditCard.getFullName(), response.fullName());
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardResponse with different data")
    void testFromCreditCardToCreditCardResponseDifferentData() {
        CreditCard creditCard = new CreditCard(
                2L,
                "5500000000000004",
                "2028-06-30",
                "456",
                "Jane Smith Roberts",
                2L);

        CreditCardResponse response = CreditCardPresentationMapper
                .fromCreditCardToCreditCardResponse(creditCard);

        assertEquals(creditCard.getSourceCardId(), response.creditCardId());
        assertEquals(creditCard.getCardNumber(), response.cardNumber());
        assertEquals(creditCard.getExpirationDate(), response.expirationDate());
        assertEquals(creditCard.getCvv(), response.cvv());
        assertEquals(creditCard.getFullName(), response.fullName());
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardResponse with null")
    void testFromCreditCardToCreditCardResponseNull() {
        CreditCardResponse response = CreditCardPresentationMapper
                .fromCreditCardToCreditCardResponse(null);

        assertNull(response);
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardSummaryResponse")
    void testFromCreditCardToCreditCardSummaryResponse() {
        CreditCard creditCard = new CreditCard(
                1L,
                "4111111111111111",
                "2027-12-31",
                "123",
                "John Doe Smith",
                1L);

        CreditCardSummaryResponse response = CreditCardPresentationMapper
                .fromCreditCardToCreditCardSummaryResponse(creditCard);

        assertEquals(creditCard.getSourceCardId(), response.creditCardId());
        assertEquals(creditCard.getCardNumber(), response.cardHolderName());
        assertEquals(creditCard.getFullName(), response.cardNumber());
        assertEquals(creditCard.getExpirationDate(), response.expirationDate());
        assertEquals(creditCard.getCvv(), response.cvv());
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardSummaryResponse with different data")
    void testFromCreditCardToCreditCardSummaryResponseDifferentData() {
        CreditCard creditCard = new CreditCard(
                3L,
                "3400000000000009",
                "2029-03-15",
                "789",
                "Michael Roberts Johnson",
                3L);

        CreditCardSummaryResponse response = CreditCardPresentationMapper
                .fromCreditCardToCreditCardSummaryResponse(creditCard);

        assertEquals(creditCard.getSourceCardId(), response.creditCardId());
        assertEquals(creditCard.getCardNumber(), response.cardHolderName());
        assertEquals(creditCard.getFullName(), response.cardNumber());
        assertEquals(creditCard.getExpirationDate(), response.expirationDate());
        assertEquals(creditCard.getCvv(), response.cvv());
    }

    @Test
    @DisplayName("Test map from CreditCard to CreditCardSummaryResponse with null")
    void testFromCreditCardToCreditCardSummaryResponseNull() {
        CreditCardSummaryResponse response = CreditCardPresentationMapper
                .fromCreditCardToCreditCardSummaryResponse(null);

        assertNull(response);
    }
}
