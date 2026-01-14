package es.VetUp.banco_back.b_domain.model;

import java.math.BigDecimal;

public class BankAccount {
    private final Long accountId;
    private final String iban;
    private final BigDecimal balance;
    private final Long userId;

    public BankAccount(Long accountId, String iban, BigDecimal balance, Long userId) {
        this.accountId = accountId;
        this.iban = iban;
        this.balance = balance;
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getIban() {
        return iban;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getUserId() {
        return userId;
    }
}
