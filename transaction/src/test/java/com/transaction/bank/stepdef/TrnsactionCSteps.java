package com.transaction.bank.stepdef;

import com.transaction.bank.dto.RequestTrxStatusDto;
import com.transaction.bank.dto.ResponseTrxStatusDto;
import com.transaction.bank.dto.TransactionDto;
import com.transaction.bank.enums.StatusEnum;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrnsactionCSteps {

    @Autowired
    private RestTemplate restTemplate;
    private ResponseTrxStatusDto responseTrxStatusDto;
    private TransactionDto transactionDto;


    @Given("^A new transaction that is stored in our system$")
    public void aNewTransactionThatIsStoredInOurSystem() {
        HttpEntity<TransactionDto> httpEntity = new HttpEntity<>(buildTransactionDto());
        ResponseEntity<TransactionDto> responseEntity =
                restTemplate.postForEntity("http://localhost:8080/trx/transaction", httpEntity,
                        TransactionDto.class);
        transactionDto = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @When("^I check the \"([^\"]*)\" from INTERNAL channel$")
    public void iCheckTheFromINTERNALChannel(String status) {
        HttpEntity<RequestTrxStatusDto> httpEntity = new HttpEntity<>(buildRequestTrxStatusDto(status));
        ResponseEntity<ResponseTrxStatusDto> responseEntity =
                restTemplate.postForEntity("http://localhost:8080/trx/transaction/status", httpEntity,
                        ResponseTrxStatusDto.class);
        this.responseTrxStatusDto = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @And("^the transaction date  is before today$")
    public void theTransactionDateIsBeforeToday() {
        assertTrue(this.responseTrxStatusDto.getDate().isBefore(LocalDateTime.now()));
    }

    @Then("^The systems returns the status 'SETTLED'$")
    public void theSystemsReturnsTheStatusSETTLED() {
        assertEquals(StatusEnum.SETTLED.name(), this.responseTrxStatusDto.getStatus());
    }

    @And("^the \"([^\"]*)\"$")
    public void the(String amount) {
        assertEquals(new BigDecimal(amount), this.responseTrxStatusDto.getAmount().setScale(2, RoundingMode.HALF_EVEN));
    }

    @And("^the \"([^\"]*)\" value$")
    public void theValue(String fee) {
        assertEquals(new BigDecimal(fee), this.responseTrxStatusDto.getFee().setScale(2, RoundingMode.HALF_EVEN));
    }

    private RequestTrxStatusDto buildRequestTrxStatusDto(String status) {
        return RequestTrxStatusDto.builder()
                .reference("12345C")
                .channel(status)
                .build();
    }

    private static TransactionDto buildTransactionDto() {
        return TransactionDto.builder()
                .description("Restaurant payment")
                .fee(new BigDecimal(3.18))
                .amount(new BigDecimal(193.38))
                .accountIban("ES9820385778983000760236")
                .reference("12345C")
                .date(ZonedDateTime.parse("2019-07-16T16:55:42.000Z").toLocalDateTime().toString())
                .build();
    }
}
