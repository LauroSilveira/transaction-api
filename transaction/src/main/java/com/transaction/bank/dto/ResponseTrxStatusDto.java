package com.transaction.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseTrxStatusDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String reference;
	private String status;
	private BigDecimal amount;
	private BigDecimal fee;
	private LocalDateTime date;
}
