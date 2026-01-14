package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.repository.BankTransactionRepository;
import es.VetUp.banco_back.b_domain.service.BankTransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BankTransactionServiceImpl implements BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionServiceImpl(BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }

    @Override
    public List<BankTransaction> getTransactionsByAccountId(Long accountId) {
        return bankTransactionRepository.findAllByAccountId(accountId);
    }

    @Override
    public List<BankTransaction> getAllTransactionsByCardId(Long sourceCardId) {
        return bankTransactionRepository.findAllByCardId(sourceCardId);
    }

    @Override
    public List<BankTransaction> getTransactionsBetweenDates(Long sourceCardId, LocalDate startDate, LocalDate endDate) {
        return bankTransactionRepository.findAllByCardIdAndDateBetween(sourceCardId, startDate, endDate);
    }

    @Override
    public List<BankTransaction> getAllTransactions() {
        return bankTransactionRepository.findAll();
    }

    @Override
    public Optional<BankTransaction> getTransactionById(Long bankMovementId) {
        return bankTransactionRepository.findById(bankMovementId);
    }

    @Override
    public BankTransaction createTransaction(BankTransaction bankTransaction) {
        return bankTransactionRepository.save(bankTransaction);
    }
}

