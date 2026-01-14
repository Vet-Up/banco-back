package es.VetUp.banco_back.c_persistence.dao.jpa.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "BankAccount")
public class BankAccountJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

//    CREATE TABLE BankAccount (
//            account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
//            balance DECIMAL(15,2) DEFAULT 0,
//    iban VARCHAR(34) UNIQUE NOT NULL,
//    user_id BIGINT NOT NULL,
//    FOREIGN KEY (user_id) REFERENCES Users(user_id)
//            );

    @Column(name = "iban", nullable = false, unique = true)
    private String iban;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;


    public BankAccountJpaEntity() {
    }

    public BankAccountJpaEntity(Long accountId, BigDecimal balance, String iban, UserJpaEntity user) {
        this.accountId = accountId;
        this.balance = balance;
        this.iban = iban;
        this.user = user;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Long getUser() {
        return user != null? user.getUserId() : null;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

}
