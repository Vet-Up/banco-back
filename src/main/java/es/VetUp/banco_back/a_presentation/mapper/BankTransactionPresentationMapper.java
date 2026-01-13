package es.VetUp.banco_back.a_presentation.mapper;

import es.VetUp.banco_back.a_presentation.webModel.response.BankTransactionResponse;
import es.VetUp.banco_back.a_presentation.webModel.response.summary.BankTransactionSummaryResponse;
import es.VetUp.banco_back.b_domain.model.BankTransaction;

public class BankTransactionPresentationMapper {

    public static BankTransactionResponse fromBankTransactionToBankTransactionResponse(BankTransaction bankTransaction){
        if (bankTransaction == null) {
            return null;
        } else {
            return new BankTransactionResponse(
                    bankTransaction.getTransactionId(),
                    bankTransaction.getType().toString(),
                    bankTransaction.getOrigin().toString(),
                    CreditCardPresentationMapper.fromCreditCardToCreditCardSummaryResponse(bankTransaction.getCreditCard()),
                    bankTransaction.getDate(),
                    bankTransaction.getAmount(),
                    bankTransaction.getDescription()
            );
        }
    }

    public static BankTransactionSummaryResponse fromBankTransactionToBankTransactionSummaryResponse(BankTransaction bankTransaction){
        if (bankTransaction == null) {
            return null;
        } else {
            return new BankTransactionSummaryResponse(
                    bankTransaction.getTransactionId(),
                    bankTransaction.getType().toString(),
                    bankTransaction.getOrigin().toString(),
                    bankTransaction.getDate(),
                    bankTransaction.getAmount(),
                    bankTransaction.getDescription()
            );
        }
    }
}
