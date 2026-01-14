package es.VetUp.banco_back.a_presentation.webModel.response.summary;

public record CreditCardSummaryResponse(
        Long creditCardId,
        String cardHolderName,
        String cardNumber,
        String expirationDate,
        String cvv
) {
}
