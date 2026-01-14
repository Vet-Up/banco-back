package es.VetUp.banco_back.c_persistence.repository;

import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.repository.BankTransactionRepository;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankTransactionJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankTransactionJpaEntity;
import es.VetUp.banco_back.c_persistence.repository.mapper.BankTransactionPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class BankTransactionRepositoryImpl implements BankTransactionRepository {
    private final BankTransactionJpaDao bankTransactionJpaDao;

    public BankTransactionRepositoryImpl(BankTransactionJpaDao bankTransactionJpaDao) {
        this.bankTransactionJpaDao = bankTransactionJpaDao;
    }


    @Override
    public Optional<BankTransaction> findById(Long bankMovementId) {
        return bankTransactionJpaDao.findByTransactionId(bankMovementId).map(BankTransactionPersistenceMapper::fromBankTransactionJpaEntityToBankTransaction);
    }

    @Override
    public List<BankTransaction> findAll() {

        return bankTransactionJpaDao.findAll().stream()
                .map(BankTransactionPersistenceMapper::fromBankTransactionJpaEntityToBankTransaction)
                .toList();

    }

    @Override
    public List<BankTransaction> findAllByAccountId(Long accountId) {
        return bankTransactionJpaDao.findAllTransactionsByAccountId(accountId).stream()
                .map(BankTransactionPersistenceMapper::fromBankTransactionJpaEntityToBankTransaction)
                .toList();

    }

    @Override
    public List<BankTransaction> findAllByCardId(Long cardId) {
        return bankTransactionJpaDao.findAllByCardId(cardId).stream()
                .map(BankTransactionPersistenceMapper::fromBankTransactionJpaEntityToBankTransaction)
                .toList();
    }

    @Override
    public List<BankTransaction> findAllByCardIdAndDateBetween(Long cardId, LocalDate startDate, LocalDate endDate) {
        return bankTransactionJpaDao.findAllByCardIdAndDateBetween(cardId, startDate, endDate).stream()
                .map(BankTransactionPersistenceMapper::fromBankTransactionJpaEntityToBankTransaction)
                .toList();
    }

    @Override
    public BankTransaction save(BankTransaction bankTransaction) {
        BankTransactionJpaEntity savedEntity = bankTransactionJpaDao.save(
                BankTransactionPersistenceMapper.fromBankTransactionToBankTransactionJpaEntity(bankTransaction)
        );

        return BankTransactionPersistenceMapper.fromBankTransactionJpaEntityToBankTransaction(savedEntity);
    }
}
