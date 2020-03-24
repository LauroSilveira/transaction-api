package com.transaction.bank.stepdef;

import com.transaction.bank.dto.RequestTrxStatusDto;
import com.transaction.bank.dto.ResponseTrxStatusDto;
import com.transaction.bank.dto.TransactionDto;
import com.transaction.bank.enums.ChannelEnum;
import com.transaction.bank.enums.StatusEnum;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TransactionBSteps {

    @Autowired
    private RestTemplate restTemplate;
    private TransactionDto transactionDto;
    private ResponseTrxStatusDto responseTrxStatusDto;

    @Given("^A transaction that is stored in our system$")
    public void a_transaction_that_is_stored_in_our_system() {
        HttpEntity<TransactionDto> httpEntity = new HttpEntity<>(buildTransactionDto());
        ResponseEntity<TransactionDto> responseEntity =
                restTemplate.postForEntity("http://localhost:8080/trx/transaction", httpEntity,
                        TransactionDto.class);
        transactionDto = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @When("^I check the status from CLIENT or ATM \"([^\"]*)\"$")
    public void i_check_the_status_from_CLIENT_or_ATM(String channel) {
        HttpEntity<RequestTrxStatusDto> httpEntity = new HttpEntity<>(buildRequestTrxStatusDto());
        ResponseEntity<ResponseTrxStatusDto> responseEntity =
                restTemplate.postForEntity("http://localhost:8080/trx/transaction/status", httpEntity,
                        ResponseTrxStatusDto.class);
        this.responseTrxStatusDto = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(channel, this.responseTrxStatusDto.getStatus());
    }

    @When("^the transaction date is before today$")
    public void the_transaction_date_is_before_today() {

    }

    @Then("^The system returns the status 'SETTLED'$")
    public void the_system_returns_the_status_SETTLED() {
        assertEquals(StatusEnum.SETTLED.name(), this.responseTrxStatusDto.getStatus());
        assertEquals(this.transactionDto.getReference(), this.responseTrxStatusDto.getReference());
        assertEquals(new BigDecimal(190.20), this.responseTrxStatusDto.getAmount());
    }

    @Then("^the amount substracting the fee$")
    public void the_amount_substracting_the_fee() {
        assertEquals(new BigDecimal(190.20), this.responseTrxStatusDto.getFee());
    }

    private static TransactionDto buildTransactionDto() {
        return TransactionDto.builder()
                .description("Restaurant payment")
                .fee(new BigDecimal(3.18))
                .amount(new BigDecimal(193.38))
                .accountIban("ES9820385778983000760236")
                .reference("12345A")
                .date(LocalDateTime.now().toString())
                .build();
    }

    private static RequestTrxStatusDto buildRequestTrxStatusDto() {
        return RequestTrxStatusDto.builder()
                .reference("12345A")
                .channel(ChannelEnum.CLIENT.name())
                .build();
    }
}
