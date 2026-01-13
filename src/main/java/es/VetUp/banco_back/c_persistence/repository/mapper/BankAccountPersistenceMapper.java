package es.VetUp.banco_back.c_persistence.repository.mapper;

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



}
