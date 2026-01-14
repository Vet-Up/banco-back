package es.VetUp.banco_back.b_domain.service;

import es.VetUp.banco_back.b_domain.service.dto.CardPaymentRequest;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentResponse;

public interface CardPaymentService {
    CardPaymentResponse processPayment(CardPaymentRequest paymentRequest);
}
