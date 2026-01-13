package es.VetUp.banco_back.c_persistence.dao.jpa.impl;

import es.VetUp.banco_back.c_persistence.dao.jpa.BankTransactionJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankTransactionJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BankTransactionJpaDaoImpl implements BankTransactionJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BankTransactionJpaEntity> findAll() {
        String sql = "SELECT b FROM BankTransactionJpaEntity b";
        try {
            return entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<BankTransactionJpaEntity> findAllTransactionsByAccountId(Long accountId) {
        String sql = "SELECT m FROM BankTransactionJpaEntity m WHERE m.bankAccount.accountId = :bankAccountId ORDER BY m.id";
        try {
            return entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                    .setParameter("bankAccountId", accountId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<BankTransactionJpaEntity> findAllByCardId(Long cardId) {
        String sql = "SELECT b FROM BankTransactionJpaEntity b " +
                "WHERE b.creditCard.id = :cardId";
        try {
            return entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                    .setParameter("cardId", cardId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<BankTransactionJpaEntity> findAllByCardIdAndDateBetween(Long cardId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT b FROM BankTransactionJpaEntity b " +
                "WHERE b.creditCard.id = :cardId " +
                "AND b.date BETWEEN :startDate AND :endDate";
        try {
            return entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                    .setParameter("cardId", cardId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<BankTransactionJpaEntity> findByTransactionId(Long id) {
        String sql = "SELECT b FROM BankTransactionJpaEntity b " +
                "WHERE b.transactionId = :id";
        try {
            BankTransactionJpaEntity result = entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
