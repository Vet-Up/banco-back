package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankTransactionJpaEntity;

public class BankTransactionPersistenceMapper {

    private static BankTransactionPersistenceMapper INSTANCE;

    private BankTransactionPersistenceMapper() {
    }

    public static BankTransactionPersistenceMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankTransactionPersistenceMapper();
        }
        return INSTANCE;
    }

    public static BankTransaction fromBankTransactionJpaEntityToBankTransaction(BankTransactionJpaEntity bankTransactionJpaEntity) {
        if (bankTransactionJpaEntity == null) {
            return null;
        }
        return new BankTransaction(
                bankTransactionJpaEntity.getTransactionId(),
                bankTransactionJpaEntity.getTransactionType(),
                bankTransactionJpaEntity.getOriginType(),
                bankTransactionJpaEntity.getCreditCard(),
                bankTransactionJpaEntity.getDate(),
                bankTransactionJpaEntity.getAmount(),
                bankTransactionJpaEntity.getDescription(),
                BankAccountPersistenceMapper.getInstance().fromBankAccountJpaEntityToBankAccount(bankTransactionJpaEntity.getBankAccount()
        );
    }

    public static BankTransactionJpaEntity fromBankTransactionToBankTransactionJpaEntity(BankTransaction bankTransaction) {
        if (bankTransaction == null) {
            return null;
        }
        return new BankTransactionJpaEntity(
                bankTransaction.getTransactionId(),
                bankTransaction.getDate(),
                bankTransaction.getAmount(),
                bankTransaction.getDescription(),
                bankTransaction.getType(),
                bankTransaction.getOrigin(),
                bankTransaction.getCreditCard(),
                BankAccountPersistenceMapper.getInstance().fromBankAccountToBankAccountJpaEntity(bankTransaction.getBankAccount()
        );
    }

}

