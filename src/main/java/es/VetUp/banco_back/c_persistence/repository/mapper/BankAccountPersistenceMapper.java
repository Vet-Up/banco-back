package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.BankAccountJpaEntity;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.UserJpaEntity;

public class BankAccountPersistenceMapper {
    private static BankAccountPersistenceMapper INSTANCE;
    private BankAccountPersistenceMapper() {
    }
    public static BankAccountPersistenceMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankAccountPersistenceMapper();
        }
        return INSTANCE;
    }


    public BankAccount fromBankAccountJpaEntityToBankAccount(BankAccountJpaEntity bankAccountJpaEntity) {
        if (bankAccountJpaEntity == null) {
            return null;
        }
        return new BankAccount(
                bankAccountJpaEntity.getAccountId(),
                bankAccountJpaEntity.getIban(),
                bankAccountJpaEntity.getBalance(),
                bankAccountJpaEntity.getUser()
        );
    }

    public BankAccountJpaEntity fromBankAccountToBankAccountJpaEntity(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }

        UserJpaEntity userJpaEntity = null;
        if (bankAccount.getUserId() != null) {
            userJpaEntity = new UserJpaEntity();
            userJpaEntity.setUserId(bankAccount.getUserId());
        }
        return new BankAccountJpaEntity(
                bankAccount.getAccountId(),
                bankAccount.getBalance(),
                bankAccount.getIban(),
                userJpaEntity
        );
    }


}
