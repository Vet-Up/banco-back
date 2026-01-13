package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import es.VetUp.banco_back.c_persistence.dao.jpa.CreditCardJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class CreditCardJpaDaoImpl implements CreditCardJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CreditCardJpaEntity> findById(Long sourceCardId) {
        String sql = "SELECT c FROM CreditCardJpaEntity c WHERE c.id = :id";
        try{
            return Optional.ofNullable(entityManager.createQuery(sql, CreditCardJpaEntity.class)
                    .setParameter("id", sourceCardId)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public List<CreditCardJpaEntity> findAllByAccountId(Long accountId) {
        String sql = "SELECT c FROM CreditCardJpaEntity c WHERE c.accountId = :accountId";
        try {
            return entityManager.createQuery(sql, CreditCardJpaEntity.class)
                    .setParameter("accountId", accountId)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public Boolean validateCreditCard(CreditCardJpaEntity creditCardJpaEntity) {
         String sql = "SELECT COUNT(c) FROM CreditCardJpaEntity c WHERE c.cardNumber = :number AND c.cvv = :cvv AND c.expirationDate = :expirationDate";
         Long count = entityManager.createQuery(sql, Long.class)
                 .setParameter("number", creditCardJpaEntity.getCardNumber())
                 .setParameter("cvv", creditCardJpaEntity.getCvv())
                 .setParameter("expirationDate", creditCardJpaEntity.getExpirationDate())
                 .getSingleResult();
         return count > 0;
    }


}
