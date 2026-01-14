package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.repository.BankAccountRepository;
import es.VetUp.banco_back.b_domain.service.BankAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> getAllByUserId(Long userId) {
        return bankAccountRepository.getAllByUserId(userId);
    }

    @Override
    public Optional<BankAccount> getByIban(String iban) {
        return bankAccountRepository.getByIban(iban);
    }

    @Override
    public Optional<BankAccount> getById(Long accountId) {
        return bankAccountRepository.getById(accountId);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public List<BankAccount> findByUserId(Long userId) {
        return bankAccountRepository.findByUserId(userId);
    }
}
