package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.a_presentation.mapper.BankTransactionPresentationMapper;
import es.VetUp.banco_back.a_presentation.webModel.response.BankTransactionResponse;
import es.VetUp.banco_back.a_presentation.webModel.response.summary.BankTransactionSummaryResponse;
import es.VetUp.banco_back.b_domain.model.BankTransaction;
import es.VetUp.banco_back.b_domain.service.BankTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bank-transactions")
public class BankTransactionController {

    private final BankTransactionService bankTransactionService;

    public BankTransactionController(BankTransactionService bankTransactionService) {
        this.bankTransactionService = bankTransactionService;
    }


    @GetMapping("/{id}")
        public ResponseEntity<BankTransactionResponse> getBankTransactionById(@PathVariable Long id) {
        BankTransactionResponse response = BankTransactionPresentationMapper.fromBankTransactionToBankTransactionResponse(bankTransactionService.getTransactionById(id).orElse(null));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<BankTransactionSummaryResponse>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<BankTransaction> transactions = bankTransactionService.getTransactionsByAccountId(accountId);
        List<BankTransactionSummaryResponse> response = transactions.stream()
                .map(BankTransactionPresentationMapper::fromBankTransactionToBankTransactionSummaryResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/by-card/{cardId}")
    public ResponseEntity<List<BankTransactionSummaryResponse>> getTransactionsByCardId(@PathVariable Long cardId) {
        List<BankTransaction> transactions = bankTransactionService.getAllTransactionsByCardId(cardId);
        List<BankTransactionSummaryResponse> response = transactions.stream()
                .map(BankTransactionPresentationMapper::fromBankTransactionToBankTransactionSummaryResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
