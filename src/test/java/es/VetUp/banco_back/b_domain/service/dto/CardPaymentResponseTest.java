package es.VetUp.banco_back.b_domain.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardPaymentResponseTest {

    @Test
    @DisplayName("Test CardPaymentResponse creation")
    void testCardPaymentResponseCreation() {
        String transactionId = "txn-12345-uuid";
        String status = "SUCCESS";
        String message = "Pago procesado correctamente";

        CardPaymentResponse response = new CardPaymentResponse(transactionId, status, message);

        assertAll("response",
                () -> assertEquals(transactionId, response.transactionId()),
                () -> assertEquals(status, response.status()),
                () -> assertEquals(message, response.message())
        );
    }

    @Test
    @DisplayName("Test CardPaymentResponse with error status")
    void testCardPaymentResponseError() {
        String transactionId = null;
        String status = "ERROR";
        String message = "Saldo insuficiente";

        CardPaymentResponse response = new CardPaymentResponse(transactionId, status, message);

        assertAll("response",
                () -> assertNull(response.transactionId()),
                () -> assertEquals(status, response.status()),
                () -> assertEquals(message, response.message())
        );
    }

    @Test
    @DisplayName("Test CardPaymentResponse equals")
    void testCardPaymentResponseEquals() {
        CardPaymentResponse response1 = new CardPaymentResponse("txn-123", "SUCCESS", "OK");
        CardPaymentResponse response2 = new CardPaymentResponse("txn-123", "SUCCESS", "OK");

        assertEquals(response1, response2);
    }

    @Test
    @DisplayName("Test CardPaymentResponse hashCode")
    void testCardPaymentResponseHashCode() {
        CardPaymentResponse response1 = new CardPaymentResponse("txn-123", "SUCCESS", "OK");
        CardPaymentResponse response2 = new CardPaymentResponse("txn-123", "SUCCESS", "OK");

        assertEquals(response1.hashCode(), response2.hashCode());
    }
}
