package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;

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
        return new CreditCard(
                creditCardJpaEntity.getSourceCardId(),
                creditCardJpaEntity.getCardNumber(),
                creditCardJpaEntity.getExpirationDate(),
                creditCardJpaEntity.getCvv(),
                creditCardJpaEntity.getFullName()
        );
    }

    public static CreditCardJpaEntity fromCreditCardToCreditCardJpaEntity(CreditCard creditCard) {
        if (creditCard == null) {
            return null;
        }

        CreditCardJpaEntity creditCardJpaEntity = new CreditCardJpaEntity();
        creditCardJpaEntity.setSourceCardId(creditCard.getSourceCardId());
        creditCardJpaEntity.setCardNumber(creditCard.getCardNumber());
        creditCardJpaEntity.setExpirationDate(creditCard.getExpirationDate());
        creditCardJpaEntity.setCvv(creditCard.getCvv());
        creditCardJpaEntity.setFullName(creditCard.getFullName());
        return creditCardJpaEntity;

    }

}
