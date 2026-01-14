package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import es.VetUp.banco_back.c_persistence.dao.jpa.CreditCardJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CreditCardJpaDaoImpl implements CreditCardJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CreditCardJpaEntity> findById(Long sourceCardId) {
        String sql = "SELECT c FROM CreditCardJpaEntity c WHERE c.sourceCardId = :id";
        try{
            return Optional.ofNullable(entityManager.createQuery(sql, CreditCardJpaEntity.class)
                    .setParameter("id", sourceCardId)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<CreditCardJpaEntity> findByCardNumber(String cardNumber) {
        String sql = "SELECT c FROM CreditCardJpaEntity c WHERE c.cardNumber = :cardNumber";
        try {
            return Optional.ofNullable(entityManager.createQuery(sql, CreditCardJpaEntity.class)
                    .setParameter("cardNumber", cardNumber)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CreditCardJpaEntity> findAllByAccountId(Long accountId) {
        String sql = "SELECT c FROM CreditCardJpaEntity c WHERE c.bankAccount.accountId = :accountId";
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

    @Override
    public Boolean isExpired(Long sourceCardId) {
        String sql = "SELECT c.expirationDate FROM CreditCardJpaEntity c WHERE c.sourceCardId = :id";
        try {
            LocalDate expirationDate = entityManager.createQuery(sql, LocalDate.class)
                    .setParameter("id", sourceCardId)
                    .getSingleResult();
            return expirationDate != null && expirationDate.isBefore(LocalDate.now());
        } catch (Exception e) {
            return true; // Si no se encuentra, consideramos que está expirada
        }
    }
}
