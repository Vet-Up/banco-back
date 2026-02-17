package es.VetUp.banco_back.a_presentation.mapper;

import es.VetUp.banco_back.a_presentation.webModel.response.BankAccountResponse;
import es.VetUp.banco_back.b_domain.model.BankAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountPresentationMapperTest {

    @Test
    @DisplayName("Test map from BankAccount to BankAccountResponse")
    void testFromBankAccountToBankAccountResponse() {
        BankAccount bankAccount = new BankAccount(
                1L,
                "ES1234567890",
                new BigDecimal("1000.50"),
                100L);

        BankAccountPresentationMapper mapper = BankAccountPresentationMapper.getInstance();
        BankAccountResponse response = mapper.fromBankAccountToBankAccountResponse(bankAccount);

        assertNotNull(response);
        assertEquals(bankAccount.getAccountId(), response.accountId());
        assertEquals(bankAccount.getIban(), response.iban());
        assertEquals(bankAccount.getBalance(), response.balance());
        assertEquals(bankAccount.getUserId(), response.userId());
    }

    @Test
    @DisplayName("Test map from BankAccount to BankAccountResponse with null input")
    void testFromBankAccountToBankAccountResponseNull() {
        BankAccountPresentationMapper mapper = BankAccountPresentationMapper.getInstance();
        BankAccountResponse response = mapper.fromBankAccountToBankAccountResponse(null);

        assertNull(response);
    }
}
