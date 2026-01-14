package es.VetUp.banco_back.b_domain.model;

import java.util.Objects;

public class CreditCard {
    private final Long sourceCardId;
    private final String cardNumber;
    private final String expirationDate;
    private final String cvv;
    private final String fullName;
    private final Long accountId;

    public CreditCard(Long sourceCardId, String cardNumber, String expirationDate, String cvv, String fullName, Long accountId) {
        this.sourceCardId = sourceCardId;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.fullName = fullName;
        this.accountId = accountId;
    }

    public Long getSourceCardId() {
        return sourceCardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getAccountId() {
        return accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(sourceCardId, that.sourceCardId) && Objects.equals(cardNumber, that.cardNumber) && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(cvv, that.cvv) && Objects.equals(fullName, that.fullName) && Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceCardId, cardNumber, expirationDate, cvv, fullName, accountId);
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "sourceCardId=" + sourceCardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", fullName='" + fullName + '\'' +
                ", accountId=" + accountId +
                '}';
    }
}
