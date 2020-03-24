package com.transaction.bank.stepdef;

import com.transaction.bank.dto.RequestTrxStatusDto;
import com.transaction.bank.dto.ResponseTrxStatusDto;
import com.transaction.bank.dto.TransactionDto;
import com.transaction.bank.enums.ChannelEnum;
import com.transaction.bank.enums.StatusEnum;
import cucumber.api.PendingException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class TransactionFSteps {

    @Autowired
    private RestTemplate restTemplate;
    private TransactionDto transactionDto;
    private ResponseTrxStatusDto responseTrxStatusDto;

    @Given("^A transaction that is stored in  our system$")
    public void aTransactionThatIsStoredInOurSystem() {
        HttpEntity<TransactionDto> httpEntity = new HttpEntity<>(buildTransactionDto());
        ResponseEntity<TransactionDto> responseEntity =
                restTemplate.postForEntity("http://localhost:8080/trx/transaction", httpEntity,
                        TransactionDto.class);
        transactionDto = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }

    @When("^I check the \"([^\"]*)\" from CLIENT channel$")
    public void iCheckTheFromCLIENTChannel(String channel)  {
        HttpEntity<RequestTrxStatusDto> httpEntity = new HttpEntity<>(buildRequestTrxStatusDto());
        ResponseEntity<ResponseTrxStatusDto> responseEntity =
                restTemplate.postForEntity("http://localhost:8080/trx/transaction/status", httpEntity,
                        ResponseTrxStatusDto.class);
        this.responseTrxStatusDto = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(channel, this.responseTrxStatusDto.getStatus());
    }

    @And("^the transaction date is greater than today$")
    public void theTransactionDateIsGreaterThanToday() {
       assertTrue(LocalDateTime.now().getYear() < this.responseTrxStatusDto.getDate().getYear());
    }

    @Then("^The system returns the \"([^\"]*)\" 'FUTURE'$")
    public void theSystemReturnsTheFUTURE(String status) {
        assertEquals(status, this.responseTrxStatusDto.getStatus());

    }

    @And("^the \"([^\"]*)\"  substracting the fee$")
    public void theSubstractingTheFee(String amount) {
        assertNotEquals(amount, this.responseTrxStatusDto.getAmount().setScale(2, RoundingMode.HALF_EVEN));

    }
    private static TransactionDto buildTransactionDto() {
        return TransactionDto.builder()
                .description("Restaurant payment")
                .fee(new BigDecimal(3.18))
                .amount(new BigDecimal(193.38))
                .accountIban("ES9820385778983000760236")
                .reference("12345F")
                .date(LocalDateTime.of(2022, 12, 25, 20, 30, 50).toString())
                .build();
    }
    private static RequestTrxStatusDto buildRequestTrxStatusDto() {
        return RequestTrxStatusDto.builder()
                .reference("12345F")
                .channel(StatusEnum.FUTURE.name())
                .build();
    }
}
