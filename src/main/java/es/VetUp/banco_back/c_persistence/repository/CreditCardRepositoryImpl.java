package es.VetUp.banco_back.c_persistence.repository;

import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.repository.CreditCardRepository;
import es.VetUp.banco_back.c_persistence.dao.jpa.CreditCardJpaDao;
import es.VetUp.banco_back.c_persistence.dao.jpa.entity.CreditCardJpaEntity;
import es.VetUp.banco_back.c_persistence.repository.mapper.CreditCardPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CreditCardRepositoryImpl implements CreditCardRepository {
    private final CreditCardJpaDao creditCardJpaDao;

    public CreditCardRepositoryImpl(CreditCardJpaDao creditCardJpaDao) {
        this.creditCardJpaDao = creditCardJpaDao;
    }

    @Override
    public Optional<CreditCard> findById(Long sourceCardId) {
        return creditCardJpaDao.findById(sourceCardId)
                .map(CreditCardPersistenceMapper::fromCreditCardJpaEntityToCreditCard);
    }

    @Override
    public Optional<CreditCard> findByCardNumber(String cardNumber) {
        return creditCardJpaDao.findByCardNumber(cardNumber)
                .map(CreditCardPersistenceMapper::fromCreditCardJpaEntityToCreditCard);
    }

    @Override
    public List<CreditCard> findAllByAccountId(Long accountId) {
        return creditCardJpaDao.findAllByAccountId(accountId).stream()
                .map(CreditCardPersistenceMapper::fromCreditCardJpaEntityToCreditCard)
                .toList();
    }

    @Override
    public Boolean validateCard(CreditCard creditCard) {
        CreditCardJpaEntity creditCardJpaEntity = CreditCardPersistenceMapper.fromCreditCardToCreditCardJpaEntity(creditCard);
        return creditCardJpaDao.validateCreditCard(creditCardJpaEntity);
    }

    @Override
    public Boolean isExpired(Long sourceCardId) {
        return creditCardJpaDao.isExpired(sourceCardId);
    }
}
