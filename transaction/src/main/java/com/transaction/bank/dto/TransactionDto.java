package com.transaction.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String reference;
	@NotBlank
	@NotEmpty(message = "accountIban must not null")
	private String accountIban;
	private String date;
	@NotNull(message = "amount must not null")
	private BigDecimal amount;
	private BigDecimal fee;
	private String description;
}
