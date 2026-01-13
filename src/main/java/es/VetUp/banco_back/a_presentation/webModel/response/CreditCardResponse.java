package es.VetUp.banco_back.a_presentation.webModel.response;

public record CreditCardResponse(
        Long creditCardId,
        String cardNumber,
        String expirationDate,
        String cvv,
        String fullName
) {
}
