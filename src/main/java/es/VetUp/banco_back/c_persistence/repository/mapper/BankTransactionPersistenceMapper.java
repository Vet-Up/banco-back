package es.VetUp.banco_back.c_persistence.repository.mapper;

import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
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
                mapTransactionTypeIdToEnum(bankTransactionJpaEntity.getTransactionTypeId()),
                mapOriginIdToEnum(bankTransactionJpaEntity.getTransactionOriginId()),
                CreditCardPersistenceMapper.fromCreditCardJpaEntityToCreditCard(bankTransactionJpaEntity.getCreditCard()),
                bankTransactionJpaEntity.getDate(),
                bankTransactionJpaEntity.getAmount(),
                bankTransactionJpaEntity.getDescription(),
                BankAccountPersistenceMapper.getInstance().fromBankAccountJpaEntityToBankAccount(bankTransactionJpaEntity.getBankAccount())
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
                mapTransactionTypeEnumToId(bankTransaction.getType()),
                mapOriginEnumToId(bankTransaction.getOrigin()),
                CreditCardPersistenceMapper.fromCreditCardToCreditCardJpaEntity(bankTransaction.getCreditCard()),
                BankAccountPersistenceMapper.getInstance().fromBankAccountToBankAccountJpaEntity(bankTransaction.getBankAccount())
        );
    }

    // Mapeo de IDs a Enums según V2__Seed_database.sql:
    // BankTransactionType: 1=Debit, 2=Credit
    // BankTransactionOrigin: 1=Transfer, 2=DirectDebit, 3=BankCard

    private static BankTransactionType mapTransactionTypeIdToEnum(Long typeId) {
        if (typeId == null) return null;
        return switch (typeId.intValue()) {
            case 1 -> BankTransactionType.Debit;
            case 2 -> BankTransactionType.Credit;
            default -> null;
        };
    }

    private static Long mapTransactionTypeEnumToId(BankTransactionType type) {
        if (type == null) return null;
        return switch (type) {
            case Debit -> 1L;
            case Credit -> 2L;
        };
    }

    private static OriginBankingMovement mapOriginIdToEnum(Long originId) {
        if (originId == null) return null;
        return switch (originId.intValue()) {
            case 1 -> OriginBankingMovement.Transfer;
            case 2 -> OriginBankingMovement.DirectDebit;
            case 3 -> OriginBankingMovement.BankCard;
            default -> null;
        };
    }

    private static Long mapOriginEnumToId(OriginBankingMovement origin) {
        if (origin == null) return null;
        return switch (origin) {
            case Transfer -> 1L;
            case DirectDebit -> 2L;
            case BankCard -> 3L;
        };
    }
}

