package es.VetUp.banco_back.a_presentation.mapper;

import es.VetUp.banco_back.a_presentation.webModel.response.BankAccountResponse;
import es.VetUp.banco_back.b_domain.model.BankAccount;

public class BankAccountPresentationMapper {

    private static BankAccountPresentationMapper INSTANCE;

    private BankAccountPresentationMapper() {
    }

    public static BankAccountPresentationMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankAccountPresentationMapper();
        }
        return INSTANCE;
    }

    public BankAccountResponse fromBankAccountToBankAccountResponse(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }
        return new BankAccountResponse(
                bankAccount.getAccountId(),
                bankAccount.getIban(),
                bankAccount.getBalance(),
                bankAccount.getUserId()
        );
    }
}
