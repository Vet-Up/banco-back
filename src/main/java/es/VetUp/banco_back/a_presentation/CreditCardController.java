package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.a_presentation.mapper.CreditCardPresentationMapper;
import es.VetUp.banco_back.a_presentation.webModel.response.CreditCardResponse;
import es.VetUp.banco_back.b_domain.model.CreditCard;
import es.VetUp.banco_back.b_domain.service.CreditCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit-cards")
@CrossOrigin("*")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("{id}")
    public ResponseEntity<CreditCardResponse> getCreditCardById(@PathVariable Long id){
        CreditCard creditCard = creditCardService.getCreditCardById(id);
        CreditCardResponse creditCardResponse = CreditCardPresentationMapper.fromCreditCardToCreditCardResponse(creditCardService.getCreditCardById(id));
        return new ResponseEntity<>(creditCardResponse, HttpStatus.OK);
    }


    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<CreditCardResponse>> getCreditCardByAccountId(@PathVariable Long accountId){
        List<CreditCard> creditCards = creditCardService.getCardsByAccountId(accountId);
        List<CreditCardResponse> creditCardResponses = creditCards.stream()
                .map(CreditCardPresentationMapper::fromCreditCardToCreditCardResponse)
                .toList();
        return new ResponseEntity<>(creditCardResponses, HttpStatus.OK);
    }
}
