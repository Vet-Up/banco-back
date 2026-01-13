package es.VetUp.banco_back.a_presentation.webModel.response;

import es.VetUp.banco_back.a_presentation.webModel.response.summary.CreditCardSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BankTransactionResponse(
        Long transactionId,
        String transactionType,
        String originType,
        CreditCardSummaryResponse creditCard,
        LocalDate date,
        BigDecimal amount,
        String description
) {
}
