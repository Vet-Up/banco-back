package es.VetUp.banco_back.b_domain.service.dto;

import es.VetUp.banco_back.b_domain.service.dto.smallDto.Authorization;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Destination;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Origin;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Pay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardPaymentRequestTest {

    @Test
    @DisplayName("Test CardPaymentRequest creation")
    void testCardPaymentRequestCreation() {
        Authorization authorization = new Authorization("testuser", "api_key_123");
        Origin origin = new Origin("4111111111111111", "2027-12-31", "123", "John Doe Smith");
        Destination destination = new Destination("ES9876543210987654321002");
        Pay payment = new Pay(100.0, "Test payment concept");

        CardPaymentRequest request = new CardPaymentRequest(authorization, origin, destination, payment);

        assertAll("request",
                () -> assertEquals(authorization, request.authorization()),
                () -> assertEquals(origin, request.origin()),
                () -> assertEquals(destination, request.destination()),
                () -> assertEquals(payment, request.payment())
        );
    }

    @Test
    @DisplayName("Test CardPaymentRequest with different data")
    void testCardPaymentRequestDifferentData() {
        Authorization authorization = new Authorization("anotheruser", "another_api_key");
        Origin origin = new Origin("5500000000000004", "2028-06-30", "456", "Jane Smith");
        Destination destination = new Destination("ES1234567890123456789001");
        Pay payment = new Pay(250.50, "Another payment");

        CardPaymentRequest request = new CardPaymentRequest(authorization, origin, destination, payment);

        assertAll("request",
                () -> assertEquals("anotheruser", request.authorization().login()),
                () -> assertEquals("5500000000000004", request.origin().cardNumber()),
                () -> assertEquals("ES1234567890123456789001", request.destination().iban()),
                () -> assertEquals(250.50, request.payment().amount())
        );
    }

    @Test
    @DisplayName("Test CardPaymentRequest equals")
    void testCardPaymentRequestEquals() {
        Authorization authorization = new Authorization("testuser", "api_key_123");
        Origin origin = new Origin("4111111111111111", "2027-12-31", "123", "John Doe");
        Destination destination = new Destination("ES9876543210987654321002");
        Pay payment = new Pay(100.0, "Test concept");

        CardPaymentRequest request1 = new CardPaymentRequest(authorization, origin, destination, payment);
        CardPaymentRequest request2 = new CardPaymentRequest(authorization, origin, destination, payment);

        assertEquals(request1, request2);
    }

    @Test
    @DisplayName("Test CardPaymentRequest hashCode")
    void testCardPaymentRequestHashCode() {
        Authorization authorization = new Authorization("testuser", "api_key_123");
        Origin origin = new Origin("4111111111111111", "2027-12-31", "123", "John Doe");
        Destination destination = new Destination("ES9876543210987654321002");
        Pay payment = new Pay(100.0, "Test concept");

        CardPaymentRequest request1 = new CardPaymentRequest(authorization, origin, destination, payment);
        CardPaymentRequest request2 = new CardPaymentRequest(authorization, origin, destination, payment);

        assertEquals(request1.hashCode(), request2.hashCode());
    }
}
