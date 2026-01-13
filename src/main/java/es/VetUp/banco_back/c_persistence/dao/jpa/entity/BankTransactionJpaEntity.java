package es.VetUp.banco_back.c_persistence.dao.jpa.entity;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "BankTransaction")
public class BankTransactionJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_date")
    private LocalDate date;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private BankTransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_type")
    private OriginBankingMovement originType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_card_id")
    private CreditCardJpaEntity creditCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccountJpaEntity bankAccount;

    public BankTransactionJpaEntity() {

    }

    public BankTransactionJpaEntity(Long transactionId, LocalDate date, BigDecimal amount, String description, BankTransactionType transactionType, OriginBankingMovement originType, CreditCardJpaEntity creditCard, BankAccountJpaEntity bankAccount) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
        this.originType = originType;
        this.creditCard = creditCard;
        this.bankAccount = bankAccount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(BankTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public OriginBankingMovement getOriginType() {
        return originType;
    }

    public void setOriginType(OriginBankingMovement originType) {
        this.originType = originType;
    }

    public CreditCardJpaEntity getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardJpaEntity creditCard) {
        this.creditCard = creditCard;
    }

    public BankAccountJpaEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountJpaEntity bankAccount) {
        this.bankAccount = bankAccount;
    }
}
