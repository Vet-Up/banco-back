package es.VetUp.banco_back.b_domain.repository;

import es.VetUp.banco_back.b_domain.model.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {
    List<BankAccount> getAllByUserId(Long userId);
    Optional<BankAccount> getByIban(String iban);
    Optional<BankAccount> getById(Long accountId);
    BankAccount save(BankAccount bankAccount);
    List<BankAccount> findByUserId(Long userId);
}
