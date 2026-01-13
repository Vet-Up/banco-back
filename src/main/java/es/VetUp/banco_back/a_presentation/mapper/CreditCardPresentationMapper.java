package es.VetUp.banco_back.a_presentation.mapper;

import es.VetUp.banco_back.a_presentation.webModel.response.CreditCardResponse;
import es.VetUp.banco_back.a_presentation.webModel.response.summary.CreditCardSummaryResponse;
import es.VetUp.banco_back.b_domain.model.CreditCard;

public class CreditCardPresentationMapper {

    private static CreditCardPresentationMapper INSTANCE;

    private CreditCardPresentationMapper(){

    }

    public static CreditCardPresentationMapper getInstance(){
        if (INSTANCE == null){
            INSTANCE = new CreditCardPresentationMapper();
        }
        return INSTANCE;
    }

    public static  CreditCardResponse fromCreditCardToCreditCardResponse(CreditCard creditCard){
        if (creditCard == null){
            return null;
        }
        return new CreditCardResponse(
            creditCard.getSourceCardId(),
            creditCard.getCardNumber(),
            creditCard.getFullName(),
            creditCard.getExpirationDate(),
            creditCard.getCvv()
        );
    }

    public static CreditCardSummaryResponse fromCreditCardToCreditCardSummaryResponse(CreditCard creditCard){
        if (creditCard == null){
            return null;
        }
        return new CreditCardSummaryResponse(
                creditCard.getSourceCardId(),
                creditCard.getCardNumber(),
                creditCard.getFullName(),
                creditCard.getExpirationDate(),
                creditCard.getCvv()

        );
    }

}
