package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.a_presentation.mapper.BankAccountPresentationMapper;
import es.VetUp.banco_back.a_presentation.webModel.response.BankAccountResponse;
import es.VetUp.banco_back.b_domain.service.BankAccountService;
import es.VetUp.banco_back.b_domain.service.CardPaymentService;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentRequest;
import es.VetUp.banco_back.b_domain.service.dto.CardPaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
@CrossOrigin(origins = "*")
public class BankAccountController {

    private final CardPaymentService cardPaymentService;
    private final BankAccountService bankAccountService;

    public BankAccountController(CardPaymentService cardPaymentService, BankAccountService bankAccountService) {
        this.cardPaymentService = cardPaymentService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<BankAccountResponse>> getBankAccountsByUserId(@PathVariable Long userId) {
        List<BankAccountResponse> response = bankAccountService.findByUserId(userId)
                .stream()
                .map(BankAccountPresentationMapper.getInstance()::fromBankAccountToBankAccountResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pago_tarjeta")
    public ResponseEntity<CardPaymentResponse> processCardPayment(@RequestBody CardPaymentRequest cardPaymentRequest) {
        CardPaymentResponse response = cardPaymentService.processPayment(cardPaymentRequest);
        return ResponseEntity.ok(response);
    }

}
