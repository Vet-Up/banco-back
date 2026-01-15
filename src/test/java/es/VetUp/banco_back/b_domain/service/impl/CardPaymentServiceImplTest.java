package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.exception.BusinessException;
import es.VetUp.banco_back.b_domain.model.BankAccount;
import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.service.BankAccountService;
import es.VetUp.banco_back.b_domain.service.BankTransactionService;
import es.VetUp.banco_back.b_domain.service.CreditCardService;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentRequest;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentResponse;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Authorization;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Destination;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Origin;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Pay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardPaymentServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private CreditCardService creditCardService;

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private BankTransactionService bankTransactionService;

    private CardPaymentServiceImpl cardPaymentService;

    private UserDto testUser;
    private CreditCard testCreditCard;
    private BankAccount sourceAccount;
    private BankAccount destinationAccount;

    @BeforeEach
    void setUp() {
        cardPaymentService = new CardPaymentServiceImpl(
                userService, creditCardService, bankAccountService, bankTransactionService);

        testUser = new UserDto(
                1L,
                "testuser",
                "password123",
                "John",
                "Doe",
                "Smith",
                "12345678A",
                "api_key_123"
        );

        testCreditCard = new CreditCard(
                1L,
                "4111111111111111",
                "2027-12-31",
                "123",
                "John Doe Smith",
                1L
        );

        sourceAccount = new BankAccount(
                1L,
                "ES1234567890123456789001",
                new BigDecimal("1000.00"),
                2L
        );

        destinationAccount = new BankAccount(
                2L,
                "ES9876543210987654321002",
                new BigDecimal("500.00"),
                1L
        );
    }

    private CardPaymentRequest createValidPaymentRequest() {
        return new CardPaymentRequest(
                new Authorization("testuser", "api_key_123"),
                new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                new Destination("ES9876543210987654321002"),
                new Pay(100.0, "Test payment concept")
        );
    }

    @Nested
    class ProcessPaymentSuccessTests {

        @Test
        @DisplayName("processPayment should succeed with valid request")
        void testProcessPaymentSuccess() {
            CardPaymentRequest request = createValidPaymentRequest();

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.of(testCreditCard));
            when(creditCardService.isExpired(1L)).thenReturn(false);
            when(bankAccountService.getById(1L)).thenReturn(Optional.of(sourceAccount));

            CardPaymentResponse response = cardPaymentService.processPayment(request);

            assertNotNull(response);
            assertEquals("SUCCESS", response.status());
            assertEquals("Pago procesado correctamente", response.message());
            assertNotNull(response.transactionId());

            verify(bankAccountService, times(2)).save(any(BankAccount.class));
            verify(bankTransactionService, times(2)).createTransaction(any());
        }
    }

    @Nested
    class AuthenticationTests {

        @Test
        @DisplayName("processPayment should throw exception when authentication fails")
        void testProcessPaymentAuthenticationFails() {
            CardPaymentRequest request = createValidPaymentRequest();

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(false);

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Credenciales de autenticación inválidas", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when user not found")
        void testProcessPaymentUserNotFound() {
            CardPaymentRequest request = createValidPaymentRequest();

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Usuario no encontrado", exception.getMessage());
        }
    }

    @Nested
    class IbanValidationTests {

        @Test
        @DisplayName("processPayment should throw exception when IBAN is null")
        void testProcessPaymentIbanNull() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination(null),
                    new Pay(100.0, "Test payment concept")
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("El IBAN es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when IBAN format is invalid")
        void testProcessPaymentIbanInvalidFormat() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination("INVALID_IBAN"),
                    new Pay(100.0, "Test payment concept")
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("El IBAN debe tener formato válido y empezar por ES", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when destination account not found")
        void testProcessPaymentDestinationAccountNotFound() {
            CardPaymentRequest request = createValidPaymentRequest();

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("La cuenta destino no existe", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when destination account doesn't belong to user")
        void testProcessPaymentDestinationAccountNotBelongsToUser() {
            CardPaymentRequest request = createValidPaymentRequest();
            BankAccount otherUserAccount = new BankAccount(2L, "ES9876543210987654321002", new BigDecimal("500.00"), 99L);

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(otherUserAccount));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("La cuenta destino debe pertenecer al usuario autenticado", exception.getMessage());
        }
    }

    @Nested
    class AmountValidationTests {

        @Test
        @DisplayName("processPayment should throw exception when amount is null")
        void testProcessPaymentAmountNull() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination("ES9876543210987654321002"),
                    new Pay(null, "Test payment concept")
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("El importe debe ser positivo", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when amount is zero")
        void testProcessPaymentAmountZero() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination("ES9876543210987654321002"),
                    new Pay(0.0, "Test payment concept")
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("El importe debe ser positivo", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when amount is negative")
        void testProcessPaymentAmountNegative() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination("ES9876543210987654321002"),
                    new Pay(-50.0, "Test payment concept")
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("El importe debe ser positivo", exception.getMessage());
        }
    }

    @Nested
    class ConceptValidationTests {

        @Test
        @DisplayName("processPayment should throw exception when concept is null")
        void testProcessPaymentConceptNull() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination("ES9876543210987654321002"),
                    new Pay(100.0, null)
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("El concepto es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when concept has less than 3 letters")
        void testProcessPaymentConceptTooFewLetters() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination("ES9876543210987654321002"),
                    new Pay(100.0, "12")
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("El concepto debe tener al menos 3 letras", exception.getMessage());
        }
    }

    @Nested
    class CreditCardValidationTests {

        @Test
        @DisplayName("processPayment should throw exception when credit card not found")
        void testProcessPaymentCreditCardNotFound() {
            CardPaymentRequest request = createValidPaymentRequest();

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Tarjeta no encontrada", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when expiration date doesn't match")
        void testProcessPaymentExpirationDateMismatch() {
            CardPaymentRequest request = createValidPaymentRequest();
            CreditCard cardWithDifferentDate = new CreditCard(1L, "4111111111111111", "2028-06-30", "123", "John Doe Smith", 1L);

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.of(cardWithDifferentDate));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Los datos de la tarjeta no coinciden", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when CVV doesn't match")
        void testProcessPaymentCvvMismatch() {
            CardPaymentRequest request = createValidPaymentRequest();
            CreditCard cardWithDifferentCvv = new CreditCard(1L, "4111111111111111", "2027-12-31", "999", "John Doe Smith", 1L);

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.of(cardWithDifferentCvv));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Los datos de la tarjeta no coinciden", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when full name doesn't match")
        void testProcessPaymentFullNameMismatch() {
            CardPaymentRequest request = createValidPaymentRequest();
            CreditCard cardWithDifferentName = new CreditCard(1L, "4111111111111111", "2027-12-31", "123", "Jane Other Name", 1L);

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.of(cardWithDifferentName));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Los datos de la tarjeta no coinciden", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when card is expired")
        void testProcessPaymentCardExpired() {
            CardPaymentRequest request = createValidPaymentRequest();

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.of(testCreditCard));
            when(creditCardService.isExpired(1L)).thenReturn(true);

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("La tarjeta está expirada", exception.getMessage());
        }
    }

    @Nested
    class BalanceValidationTests {

        @Test
        @DisplayName("processPayment should throw exception when source account not found")
        void testProcessPaymentSourceAccountNotFound() {
            CardPaymentRequest request = createValidPaymentRequest();

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.of(testCreditCard));
            when(creditCardService.isExpired(1L)).thenReturn(false);
            when(bankAccountService.getById(1L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Cuenta asociada a la tarjeta no encontrada", exception.getMessage());
        }

        @Test
        @DisplayName("processPayment should throw exception when insufficient balance")
        void testProcessPaymentInsufficientBalance() {
            CardPaymentRequest request = new CardPaymentRequest(
                    new Authorization("testuser", "api_key_123"),
                    new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith"),
                    new Destination("ES9876543210987654321002"),
                    new Pay(2000.0, "Test payment concept")
            );

            when(userService.authenticate("testuser", "api_key_123")).thenReturn(true);
            when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(bankAccountService.getByIban("ES9876543210987654321002")).thenReturn(Optional.of(destinationAccount));
            when(creditCardService.findByCardNumber("4111111111111111")).thenReturn(Optional.of(testCreditCard));
            when(creditCardService.isExpired(1L)).thenReturn(false);
            when(bankAccountService.getById(1L)).thenReturn(Optional.of(sourceAccount));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> cardPaymentService.processPayment(request));

            assertEquals("Saldo insuficiente en la cuenta asociada a la tarjeta", exception.getMessage());
        }
    }
}
