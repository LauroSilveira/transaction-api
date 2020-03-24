package com.transaction.bank.service;

import com.transaction.bank.dto.TransactionDto;

public interface TransactionService {
	
	public void saveTransaction(TransactionDto dto);
}