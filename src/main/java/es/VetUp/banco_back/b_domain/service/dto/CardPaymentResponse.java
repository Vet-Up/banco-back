package es.VetUp.banco_back.b_domain.service.dto;

public record CardPaymentResponse(
    String transactionId,
    String status,
    String message
) {
}
