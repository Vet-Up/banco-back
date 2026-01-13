package es.VetUp.banco_back.b_domain.model;

import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class BankTransaction {
    private final Long transactionId;
    private final BankTransactionType type;
    private final OriginBankingMovement origin;
    private final CreditCard creditCard;
    private final LocalDate date;
    private final BigDecimal amount;
    private final String description;
    private final BankAccount bankAccount;


    public BankTransaction(Long transactionId, BankTransactionType type, OriginBankingMovement origin, CreditCard creditCard, LocalDate date, BigDecimal amount, String description, BankAccount bankAccount) {
        this.transactionId = transactionId;
        this.type = type;
        this.origin = origin;
        this.creditCard = creditCard;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.bankAccount = bankAccount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public BankTransactionType getType() {
        return type;
    }

    public OriginBankingMovement getOrigin() {
        return origin;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankTransaction that = (BankTransaction) o;
        return Objects.equals(transactionId, that.transactionId) && type == that.type && origin == that.origin && Objects.equals(creditCard, that.creditCard) && Objects.equals(date, that.date) && Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && Objects.equals(bankAccount, that.bankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, type, origin, creditCard, date, amount, description, bankAccount);
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "transactionId=" + transactionId +
                ", type=" + type +
                ", origin=" + origin +
                ", creditCard=" + creditCard +
                ", date=" + date +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", bankAccount=" + bankAccount +
                '}';
    }
}
