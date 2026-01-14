package es.VetUp.banco_back.b_domain.service;

import es.VetUp.banco_back.b_domain.model.BankTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BankTransactionService {
    List<BankTransaction> getAllTransactions();
    Optional<BankTransaction> getTransactionById(Long bankMovementId);
    List<BankTransaction> getTransactionsByAccountId(Long accountId);
    List<BankTransaction> getAllTransactionsByCardId(Long sourceCardId);
    List<BankTransaction> getTransactionsBetweenDates(Long sourceCardId, LocalDate startDate, LocalDate endDate);
    BankTransaction createTransaction(BankTransaction bankTransaction);
}
