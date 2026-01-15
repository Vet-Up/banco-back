package es.VetUp.banco_back.c_persistence.dao.jpa;

import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;

import java.util.List;
import java.util.Optional;

public interface CreditCardJpaDao {
    Optional<CreditCardJpaEntity> findById(Long sourceCardId);
    Optional<CreditCardJpaEntity> findByCardNumber(String cardNumber);
    List<CreditCardJpaEntity> findAllByAccountId(Long accountId);
    Boolean validateCreditCard(CreditCardJpaEntity creditCardJpaEntity);
    Boolean isExpired(Long sourceCardId);
    List<CreditCardJpaEntity> findByAccountId(Long accountId);
}
