package es.VetUp.banco_back.c_persistence.dao.jpa;

import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;

import java.util.List;
import java.util.Optional;

public interface CreditCardJpaDao {
    Optional<CreditCardJpaEntity> findById(Long sourceCardId);
    List<CreditCardJpaEntity> findAllByAccountId(Long accountId);
    Boolean validateCreditCard(CreditCardJpaEntity creditCardJpaEntity);
}
