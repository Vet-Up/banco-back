package es.VetUp.banco_back.b_domain.repository;

import es.VetUp.banco_back.b_domain.model.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository {
    Optional<CreditCard> findById(Long sourceCardId);
    List<CreditCard> findAllByAccountId(Long accountId);
    Boolean validateCard(CreditCard creditCard);
    Boolean isExpired(Long sourceCardId);
}
