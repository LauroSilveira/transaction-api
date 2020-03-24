package com.transaction.bank.repository;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transaction.bank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<Transaction> findByAccountIban(String accountIban, Sort sort);

	Optional<Transaction> findByReference(String reference);
	
}
