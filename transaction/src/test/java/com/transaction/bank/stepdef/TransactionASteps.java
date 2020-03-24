package com.transaction.bank.stepdef;

import com.transaction.bank.dto.RequestTrxStatusDto;
import com.transaction.bank.dto.ResponseTrxStatusDto;
import com.transaction.bank.dto.TransactionDto;
import com.transaction.bank.enums.StatusEnum;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class TransactionASteps {
    @Autowired
    private RestTemplate restTemplate;
    private ResponseTrxStatusDto responseTrxStatusDto;
    private TransactionDto transactionDto;

    @Given("^A transaction that is not stored in our system$")
    public void a_transaction_that_is_not_stored_in_our_system() {
        ResponseEntity<TransactionDto> responseEntity = null;
        try {
            HttpEntity<TransactionDto> httpEntity = new HttpEntity<>(buildTransactionDToMissingFields());
            responseEntity = restTemplate.postForEntity("http://localhost:8080/trx/transaction", httpEntity,
                    TransactionDto.class);
            transactionDto = responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            assertNull(responseEntity);
        }
    }


    @When("^I check the status from any \"([^\"]*)\"$")
    public void i_check_the_status_from_any(String status) {
        HttpEntity<RequestTrxStatusDto> httpEntity = new HttpEntity<>(buildRequestTrxStatusDto(status));
        ResponseEntity<ResponseTrxStatusDto> responseEntity =
                restTemplate.postForEntity("http://localhost:8080/trx/transaction/status", httpEntity,
                        ResponseTrxStatusDto.class);
        this.responseTrxStatusDto = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Then("^The system returns the status INVALID$")
    public void the_system_returns_the_status_INVALID() {
        assertEquals(StatusEnum.INVALID.name(), this.responseTrxStatusDto.getStatus());
    }

    private static RequestTrxStatusDto buildRequestTrxStatusDto(String channelStatus) {
        return RequestTrxStatusDto.builder()
                .reference("XXXXXX")
                .channel(channelStatus)
                .build();
    }

    private static TransactionDto buildTransactionDToMissingFields() {
        return TransactionDto.builder()
                .reference("12345A")
                .date("2019-07-16T16:55:42.000Z")
                .fee(new BigDecimal(3.18))
                .description("Transaction not stored")
                .build();
    }
}
