package es.VetUp.banco_back.b_domain.service;

import es.VetUp.banco_back.b_domain.model.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    CreditCard getCreditCardById(Long sourceCardId);
    Optional<CreditCard> findByCardNumber(String cardNumber);
    List<CreditCard> getAllCreditCardsByUser(Long userId);
    boolean validateCreditCardDetails(CreditCard creditCard);
    boolean isExpired(Long sourceCardId);
    List<CreditCard> getCardsByAccountId(Long accountId);
}
