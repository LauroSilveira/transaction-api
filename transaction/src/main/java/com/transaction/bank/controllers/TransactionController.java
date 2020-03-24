package com.transaction.bank.controllers;

import javax.validation.Valid;

import com.transaction.bank.dto.ResponseTrxStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.bank.dto.RequestTrxStatusDto;
import com.transaction.bank.dto.TransactionDto;
import com.transaction.bank.service.TransactionServiceImp;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionServiceImp service;

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto dto) {
        service.saveTransaction(dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<TransactionDto> searchTansaction(@RequestParam("accountIban") String accountIban, Sort sort) {
        TransactionDto dto = service.searchTransaction(accountIban, sort);
        return new ResponseEntity<>(dto, HttpStatus.FOUND);
    }

    @PostMapping(path = "/status")
    public ResponseEntity<ResponseTrxStatusDto> transactionStatus(@RequestBody @Valid RequestTrxStatusDto requestTrxDto) {
        ResponseTrxStatusDto response = service.isValidChannelStatus(requestTrxDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
