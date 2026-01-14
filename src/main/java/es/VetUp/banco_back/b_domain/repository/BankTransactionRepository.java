package es.VetUp.banco_back.b_domain.repository;

import es.VetUp.banco_back.b_domain.model.BankTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BankTransactionRepository {
    Optional<BankTransaction> findById(Long bankMovementId);
    List<BankTransaction> findAll();
    List<BankTransaction> findAllByAccountId(Long accountId);
    List<BankTransaction> findAllByCardId(Long cardId);
    List<BankTransaction> findAllByCardIdAndDateBetween(Long cardId, LocalDate startDate, LocalDate endDate);
    BankTransaction save(BankTransaction bankTransaction);
}
