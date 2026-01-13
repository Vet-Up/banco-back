package es.VetUp.banco_back.b_domain.service;

import es.VetUp.banco_back.b_domain.model.CreditCard;

import java.util.List;

public interface CreditCardService {
    CreditCard getCreditCardById(Long sourceCardId);
    List<CreditCard> getAllCreditCardsByUser(Long userId);
    boolean validateCreditCardDetails(CreditCard creditCard);
    boolean isExpired(Long sourceCardId);




}
