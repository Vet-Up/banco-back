package es.VetUp.banco_back.c_persistence.dao.jpa;

import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;

import java.util.List;
import java.util.Optional;

public interface BankAccountJpaDao {
    List<BankAccountJpaEntity> getAllByUserId(Long userId);
    Optional<BankAccountJpaEntity> getByIban(String iban);
    Optional<BankAccountJpaEntity> getById(Long accountId);
    BankAccountJpaEntity save(BankAccountJpaEntity bankAccountJpaEntity);
}