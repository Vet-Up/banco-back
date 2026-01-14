package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreditCardPersistenceMapper {

    private static CreditCardPersistenceMapper INSTANCE;

    private CreditCardPersistenceMapper() {
    }

    public static CreditCardPersistenceMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditCardPersistenceMapper();
        }
        return INSTANCE;
    }

    public static CreditCard fromCreditCardJpaEntityToCreditCard(CreditCardJpaEntity creditCardJpaEntity) {
        if (creditCardJpaEntity == null) {
            return null;
        }
        String expirationDateStr = creditCardJpaEntity.getExpirationDate() != null
                ? creditCardJpaEntity.getExpirationDate().toString()
                : null;
        return new CreditCard(
                creditCardJpaEntity.getSourceCardId(),
                creditCardJpaEntity.getCardNumber(),
                expirationDateStr,
                creditCardJpaEntity.getCvv(),
                creditCardJpaEntity.getFullName(),
                creditCardJpaEntity.getBankAccount() != null ? creditCardJpaEntity.getBankAccount().getAccountId() : null
        );
    }

    public static CreditCardJpaEntity fromCreditCardToCreditCardJpaEntity(CreditCard creditCard) {
        if (creditCard == null) {
            return null;
        }

        CreditCardJpaEntity creditCardJpaEntity = new CreditCardJpaEntity();
        creditCardJpaEntity.setSourceCardId(creditCard.getSourceCardId());
        creditCardJpaEntity.setCardNumber(creditCard.getCardNumber());
        if (creditCard.getExpirationDate() != null) {
            creditCardJpaEntity.setExpirationDate(LocalDate.parse(creditCard.getExpirationDate()));
        }
        creditCardJpaEntity.setCvv(creditCard.getCvv());
        creditCardJpaEntity.setFullName(creditCard.getFullName());
        if (creditCard.getAccountId() != null) {
            BankAccountJpaEntity bankAccountJpaEntity = new BankAccountJpaEntity();
            bankAccountJpaEntity.setAccountId(creditCard.getAccountId());
            creditCardJpaEntity.setBankAccount(bankAccountJpaEntity);
        }
        return creditCardJpaEntity;

    }

}
