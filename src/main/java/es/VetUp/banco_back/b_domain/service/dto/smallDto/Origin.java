package es.VetUp.banco_back.b_domain.service.dto.smallDto;

public record Origin(
        String cardNumber,
        String expirationDate,
        String cvc,
        String fullName
) {
}
