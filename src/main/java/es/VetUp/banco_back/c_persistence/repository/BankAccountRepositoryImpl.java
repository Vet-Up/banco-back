package es.VetUp.banco_back.c_persistence.repository;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.repository.BankAccountRepository;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankAccountJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.repository.mapper.BankAccountPersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final BankAccountJpaDao bankAccountJpaDao;

    public BankAccountRepositoryImpl(BankAccountJpaDao bankAccountJpaDao) {
        this.bankAccountJpaDao = bankAccountJpaDao;
    }

    @Override
    public List<BankAccount> getAllByUserId(Long userId) {
        return bankAccountJpaDao.getAllByUserId(userId).stream()
                .map(BankAccountPersistenceMapper.getInstance()::fromBankAccountJpaEntityToBankAccount)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BankAccount> getByIban(String iban) {
        return bankAccountJpaDao.getByIban(iban)
                .map(BankAccountPersistenceMapper.getInstance()::fromBankAccountJpaEntityToBankAccount);
    }

    @Override
    public Optional<BankAccount> getById(Long accountId) {
        return bankAccountJpaDao.getById(accountId)
                .map(BankAccountPersistenceMapper.getInstance()::fromBankAccountJpaEntityToBankAccount);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        BankAccountJpaEntity jpaEntity = BankAccountPersistenceMapper.getInstance()
                .fromBankAccountToBankAccountJpaEntity(bankAccount);
        BankAccountJpaEntity savedEntity = bankAccountJpaDao.save(jpaEntity);
        return toDomainModel(savedEntity);
    }

    @Override
    public List<BankAccount> findByUserId(Long userId) {
        return getAllByUserId(userId);
    }

    private BankAccount toDomainModel(BankAccountJpaEntity jpaEntity) {
        return BankAccountPersistenceMapper.getInstance()
                .fromBankAccountJpaEntityToBankAccount(jpaEntity);
    }
}

