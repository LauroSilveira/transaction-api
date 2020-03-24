package com.transaction.bank.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.transaction.bank.model.Transaction;

@Component
public class MappingsDto {

    public Transaction dtoToEntity(TransactionDto dto) {
        return Transaction.builder()
                .accountIban(dto.getAccountIban())
                .description(dto.getDescription())
                .reference(dto.getReference())
                .amount(dto.getAmount())
                .date(parseDate(dto.getDate()))
                .fee(dto.getFee())
                .build();
    }

    public TransactionDto entityToDto(Optional<Transaction> entity) {
        if (entity.isPresent()) {
            Transaction transaction = entity.get();
            return TransactionDto.builder()
                    .accountIban(transaction.getAccountIban())
                    .amount(transaction.getAmount())
                    .description(transaction.getDescription())
                    .fee(transaction.getFee())
                    .date(parseDateToLocalDateTime(transaction.getDate()))
                    .reference(transaction.getReference())
                    .build();
        } else {
            return TransactionDto.builder().build();
        }
    }

    private static LocalDateTime parseDate(String date) {
       return  LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private static String parseDateToLocalDateTime(LocalDateTime date) {
        return date.toString();
    }
}
