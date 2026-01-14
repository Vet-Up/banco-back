package es.VetUp.banco_back.b_domain.repository.entity;

import java.math.BigDecimal;

public record BankAccountEntity(
        Long bankAccountId,
        String iban,
        BigDecimal balance,
        Long userId
) {
}
