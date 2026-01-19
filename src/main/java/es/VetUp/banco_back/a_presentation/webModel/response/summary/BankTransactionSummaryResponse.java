package es.VetUp.banco_back.a_presentation.webModel.response.summary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BankTransactionSummaryResponse(
        Long transactionId,
        String transactionType,
        String originType,
        LocalDateTime date,
        BigDecimal amount,
        String description
) {
}
