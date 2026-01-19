package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.exception.BusinessException;
import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.model.enums.BankTransactionType;
import es.VetUp.banco_back.b_domain.model.enums.OriginBankingMovement;
import es.VetUp.banco_back.b_domain.service.*;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentRequest;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentResponse;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Destination;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Origin;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Pay;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class CardPaymentServiceImpl implements CardPaymentService {

    private static final Pattern IBAN_PATTERN = Pattern.compile("^ES\\d{22}$");
    private static final int MIN_CONCEPT_LETTERS = 3;

    private final UserService userService;
    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;

    public CardPaymentServiceImpl(UserService userService,
                                   CreditCardService creditCardService,
                                   BankAccountService bankAccountService,
                                   BankTransactionService bankTransactionService) {
        this.userService = userService;
        this.creditCardService = creditCardService;
        this.bankAccountService = bankAccountService;
        this.bankTransactionService = bankTransactionService;
    }

    @Override
    @Transactional
    public CardPaymentResponse processPayment(CardPaymentRequest paymentRequest) {

        UserDto user = authenticateUser(paymentRequest);

        Destination destination = paymentRequest.destination();
        validateIban(destination.iban());

        BankAccount destinationAccount = validateDestinationAccountBelongsToUser(destination.iban(), user.userId());

        Pay payment = paymentRequest.payment();
        validateAmount(payment.amount());

        validateConcept(payment.concept());

        Origin origin = paymentRequest.origin();
        CreditCard creditCard = validateAndGetCreditCard(origin);

        BankAccount sourceAccount = validateSufficientBalance(creditCard, payment.amount());

        processTransfer(creditCard, sourceAccount, destinationAccount, payment);

        String transactionId = UUID.randomUUID().toString();
        return new CardPaymentResponse(transactionId, "SUCCESS", "Pago procesado correctamente");
    }

    private UserDto authenticateUser(CardPaymentRequest request) {
        String username = request.authorization().login();
        String apiKey = request.authorization().apiToken();
        boolean isAuthenticated = userService.authenticate(username, apiKey);
        if (!isAuthenticated) {
            throw new BusinessException("Credenciales de autenticación inválidas");
        }
        return userService.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
    }

    private void validateIban(String iban) {
        if (iban == null || iban.isBlank()) {
            throw new BusinessException("El IBAN es obligatorio");
        }
        if (!IBAN_PATTERN.matcher(iban).matches()) {
            throw new BusinessException("El IBAN debe tener formato válido y empezar por ES");
        }
    }

    private BankAccount validateDestinationAccountBelongsToUser(String iban, Long userId) {
        BankAccount account = bankAccountService.getByIban(iban)
                .orElseThrow(() -> new BusinessException("La cuenta destino no existe"));

        if (!account.getUserId().equals(userId)) {
            throw new BusinessException("La cuenta destino debe pertenecer al usuario autenticado");
        }
        return account;
    }

    private void validateAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new BusinessException("El importe debe ser positivo");
        }
    }

    private void validateConcept(String concept) {
        if (concept == null || concept.isBlank()) {
            throw new BusinessException("El concepto es obligatorio");
        }
        long letterCount = concept.chars().filter(Character::isLetter).count();
        if (letterCount < MIN_CONCEPT_LETTERS) {
            throw new BusinessException("El concepto debe tener al menos 3 letras");
        }
    }

    private CreditCard validateAndGetCreditCard(Origin origin) {

        CreditCard creditCard = creditCardService.findByCardNumber(origin.cardNumber())
                .orElseThrow(() -> new BusinessException("Tarjeta no encontrada"));

        if (!creditCard.getExpirationDate().equals(origin.expirationDate())) {
            throw new BusinessException("Los datos de la tarjeta no coinciden");
        }
        if (!creditCard.getCvv().equals(origin.cvc())) {
            throw new BusinessException("Los datos de la tarjeta no coinciden");
        }
        if (!creditCard.getFullName().equalsIgnoreCase(origin.fullName())) {
            throw new BusinessException("Los datos de la tarjeta no coinciden");
        }

        if (creditCardService.isExpired(creditCard.getSourceCardId())) {
            throw new BusinessException("La tarjeta está expirada");
        }

        return creditCard;
    }

    private BankAccount validateSufficientBalance(CreditCard creditCard, Double amount) {
        BankAccount sourceAccount = bankAccountService.getById(creditCard.getAccountId())
                .orElseThrow(() -> new BusinessException("Cuenta asociada a la tarjeta no encontrada"));

        BigDecimal amountDecimal = BigDecimal.valueOf(amount);
        if (sourceAccount.getBalance().compareTo(amountDecimal) < 0) {
            throw new BusinessException("Saldo insuficiente en la cuenta asociada a la tarjeta");
        }

        return sourceAccount;
    }

    private void processTransfer(CreditCard creditCard, BankAccount sourceAccount,
                                  BankAccount destinationAccount, Pay payment) {
        BigDecimal amount = BigDecimal.valueOf(payment.amount());

        // Debitar de la cuenta origen
        BankAccount updatedSourceAccount = new BankAccount(
                sourceAccount.getAccountId(),
                sourceAccount.getIban(),
                sourceAccount.getBalance().subtract(amount),
                sourceAccount.getUserId()
        );
        bankAccountService.save(updatedSourceAccount);

        // Acreditar en la cuenta destino
        BankAccount updatedDestinationAccount = new BankAccount(
                destinationAccount.getAccountId(),
                destinationAccount.getIban(),
                destinationAccount.getBalance().add(amount),
                destinationAccount.getUserId()
        );
        bankAccountService.save(updatedDestinationAccount);

        // Crear transacción de débito
        BankTransaction debitTransaction = new BankTransaction(
                null,
                BankTransactionType.Debit,
                OriginBankingMovement.BankCard,
                creditCard,
                LocalDateTime.now(),
                amount,
                payment.concept(),
                updatedSourceAccount
        );
        bankTransactionService.createTransaction(debitTransaction);

        // Crear transacción de crédito
        BankTransaction creditTransaction = new BankTransaction(
                null,
                BankTransactionType.Credit,
                OriginBankingMovement.BankCard,
                creditCard,
                LocalDateTime.now(),
                amount,
                payment.concept(),
                updatedDestinationAccount
        );
        bankTransactionService.createTransaction(creditTransaction);
    }
}
