package es.VetUp.banco_back.c_persistence.dao.jpa.entity;

import es.VetUp.banco_back.b_domain.model.BankTransaction;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "CreditCard")
public class CreditCardJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_card_id")
    private Long sourceCardId;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    @Column(name = "cvv", nullable = false)
    private String cvv;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccountJpaEntity bankAccount;

    @OneToMany(mappedBy = "originCreditCard", fetch = FetchType.LAZY)
    private List<BankTransactionJpaEntity> movements;

    public CreditCardJpaEntity() {
    }

    public CreditCardJpaEntity(Long sourceCardId, String cardNumber, String expirationDate, String cvv, String fullName, BankAccountJpaEntity bankAccount, List<BankTransactionJpaEntity> movements) {
        this.sourceCardId = sourceCardId;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.fullName = fullName;
        this.bankAccount = bankAccount;
        this.movements = movements;
    }

    public Long getSourceCardId() {
        return sourceCardId;
    }

    public void setSourceCardId(Long sourceCardId) {
        this.sourceCardId = sourceCardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BankAccountJpaEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountJpaEntity bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<BankTransactionJpaEntity> getMovements() {
        return movements;
    }

    public void setMovements(List<BankTransactionJpaEntity> movements) {
        this.movements = movements;
    }
}

