package com.transaction.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestTrxStatusDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "field reference must not empty")
	@NotBlank(message = "field reference must not blank")
	private String reference;
	private String channel;
}
