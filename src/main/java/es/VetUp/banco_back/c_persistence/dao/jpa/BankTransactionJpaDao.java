package es.VetUp.banco_back.c_persistence.dao.jpa;

import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankTransactionJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BankTransactionJpaDao {
    List<BankTransactionJpaEntity> findAll();
    List<BankTransactionJpaEntity> findAllTransactionsByAccountId(Long accountId);
    List<BankTransactionJpaEntity> findAllByCardId(Long cardId);
    List<BankTransactionJpaEntity> findAllByCardIdAndDateBetween(Long cardId, LocalDate startDate,LocalDate endDate);
    Optional<BankTransactionJpaEntity> findByTransactionId(Long id);


}
