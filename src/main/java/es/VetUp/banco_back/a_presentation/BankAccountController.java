package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.b_domain.service.CardPaymentService;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentRequest;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank-accounts")
@CrossOrigin(origins = "*")
public class BankAccountController {

    private final CardPaymentService cardPaymentService;

    public BankAccountController(CardPaymentService cardPaymentService) {
        this.cardPaymentService = cardPaymentService;
    }

    @PostMapping("/pago_tarjeta")
    public ResponseEntity<CardPaymentResponse> processCardPayment(@RequestBody CardPaymentRequest cardPaymentRequest) {
        CardPaymentResponse response = cardPaymentService.processPayment(cardPaymentRequest);
        return ResponseEntity.ok(response);
    }

}
