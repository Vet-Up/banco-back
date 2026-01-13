package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.repository.CreditCardRepository;
import es.VetUp.banco_back.b_domain.service.CreditCardService;

import java.util.List;

public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    @Override
    public CreditCard getCreditCardById(Long sourceCardId) {
        return creditCardRepository.findById(sourceCardId).map(creditCard -> creditCard).orElseThrow(() -> new RuntimeException("Credit card with id " + sourceCardId + " not found"));
    }

    @Override
    public List<CreditCard> getAllCreditCardsByUser(Long userId) {
        return creditCardRepository.findAllByAccountId(userId);
    }

    @Override
    public boolean validateCreditCardDetails(CreditCard creditCard) {
        return creditCardRepository.validateCard(creditCard);
    }

    @Override
    public boolean isExpired(Long sourceCardId) {
        return creditCardRepository.isExpired(sourceCardId);
    }
}
