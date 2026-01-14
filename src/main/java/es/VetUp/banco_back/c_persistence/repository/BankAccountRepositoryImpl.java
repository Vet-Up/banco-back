package es.VetUp.banco_back.c_persistence.repository;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.repository.BankAccountRepository;
import es.VetUp.banco_back.b_domain.repository.entity.BankAccountEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.BankAccountJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.repository.mapper.BankAccountPersistenceMapper;
import org.springframework.stereotype.Repository;

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
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BankAccount> getByIban(String iban) {
        return bankAccountJpaDao.getByIban(iban)
                .map(this::toDomainModel);
    }

    @Override
    public Optional<BankAccount> getById(Long accountId) {
        return bankAccountJpaDao.getById(accountId)
                .map(this::toDomainModel);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        BankAccountEntity entity = new BankAccountEntity(
                bankAccount.getAccountId(),
                bankAccount.getIban(),
                bankAccount.getBalance(),
                bankAccount.getUserId()
        );
        BankAccountJpaEntity jpaEntity = BankAccountPersistenceMapper.getInstance()
                .fromBankAccountEntityToBankAccountJpaEntity(entity);
        BankAccountJpaEntity savedEntity = bankAccountJpaDao.save(jpaEntity);
        return toDomainModel(savedEntity);
    }

    @Override
    public List<BankAccount> findByUserId(Long userId) {
        return getAllByUserId(userId);
    }

    private BankAccount toDomainModel(BankAccountJpaEntity jpaEntity) {
        return new BankAccount(
                jpaEntity.getAccountId(),
                jpaEntity.getIban(),
                jpaEntity.getBalance(),
                jpaEntity.getUser()
        );
    }
}

