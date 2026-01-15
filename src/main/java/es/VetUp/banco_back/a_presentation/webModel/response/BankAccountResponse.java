package es.VetUp.banco_back.a_presentation.webModel.response;

import java.math.BigDecimal;

public record BankAccountResponse(
        Long accountId,
        String iban,
        BigDecimal balance,
        Long userId
) {
}
