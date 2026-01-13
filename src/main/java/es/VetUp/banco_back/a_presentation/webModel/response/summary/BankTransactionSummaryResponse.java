package es.VetUp.banco_back.a_presentation.webModel.response.summary;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BankTransactionSummaryResponse(
        Long transactionId,
        String transactionType,
        String originType,
        LocalDate date,
        BigDecimal amount,
        String description
) {
}
