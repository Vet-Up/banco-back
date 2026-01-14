package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import es.VetUp.banco_back.c_persistence.dao.jpa.BankAccountJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class BankAccountJpaDaoImpl implements BankAccountJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BankAccountJpaEntity> getAllByUserId(Long userId) {
        String sql = "SELECT b FROM BankAccountJpaEntity b WHERE b.user.userId = :userId";
        return entityManager.createQuery(sql, BankAccountJpaEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<BankAccountJpaEntity> getByIban(String iban) {
        String sql = "SELECT b FROM BankAccountJpaEntity b WHERE b.iban = :iban";
        try {
            return Optional.of(entityManager.createQuery(sql, BankAccountJpaEntity.class)
                    .setParameter("iban", iban)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<BankAccountJpaEntity> getById(Long accountId) {
        BankAccountJpaEntity entity = entityManager.find(BankAccountJpaEntity.class, accountId);
        return Optional.ofNullable(entity);
    }

    @Override
    @Transactional
    public BankAccountJpaEntity save(BankAccountJpaEntity bankAccountJpaEntity) {
        if (bankAccountJpaEntity.getAccountId() == null) {
            entityManager.persist(bankAccountJpaEntity);
            return bankAccountJpaEntity;
        } else {
            return entityManager.merge(bankAccountJpaEntity);
        }
    }
}

