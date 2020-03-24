package com.transaction.bank.service;

import com.transaction.bank.dto.MappingsDto;
import com.transaction.bank.dto.RequestTrxStatusDto;
import com.transaction.bank.dto.ResponseTrxStatusDto;
import com.transaction.bank.dto.TransactionDto;
import com.transaction.bank.enums.ChannelEnum;
import com.transaction.bank.enums.StatusEnum;
import com.transaction.bank.model.Transaction;
import com.transaction.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    private MappingsDto mappingsDto;
    @Autowired
    private TransactionRepository repository;

    @Override
    public void saveTransaction(TransactionDto dto) {
        repository.save(mappingsDto.dtoToEntity(dto));
    }

    public TransactionDto searchTransaction(String accountIban, Sort sort) {
        return mappingsDto.entityToDto(repository.findByAccountIban(accountIban, sort));
    }

    public ResponseTrxStatusDto isValidChannelStatus(RequestTrxStatusDto requestTrxDto) {
        final List<ChannelEnum> channelEnums = Arrays.asList(ChannelEnum.values());
        ResponseTrxStatusDto responseTrxStatusDto = null;
        if (channelEnums.stream().anyMatch(c -> c.name().equals(requestTrxDto.getChannel()))) {
            responseTrxStatusDto = buildResponseTrxStatusDto(requestTrxDto.getReference());
        }
        Optional<Transaction> entity = repository.findByReference(requestTrxDto.getReference());
        if (entity.isPresent()) {
            if (ChannelEnum.CLIENT.name().equals(requestTrxDto.getChannel()) && entity.get().getDate().isBefore(LocalDateTime.now())) {
                responseTrxStatusDto = buildResponseTrxClientOrAtm(requestTrxDto.getReference());
            } else if (ChannelEnum.INTERNAL.name().equals(requestTrxDto.getChannel()) && entity.get().getDate().isBefore(LocalDateTime.now())) {
                responseTrxStatusDto = buildResponseTrxValidateInternal(entity);
            } else if (StatusEnum.FUTURE.name().equals(requestTrxDto.getChannel()) && entity.get().getDate().getYear() > LocalDateTime.now().getYear()) {
                responseTrxStatusDto = buildResponseTrxValidaFuture(entity);
            }
        }
        return responseTrxStatusDto;
    }

    private ResponseTrxStatusDto buildResponseTrxStatusDto(String reference) {
        return ResponseTrxStatusDto.builder()
                .reference(reference)
                .status(StatusEnum.INVALID.name())
                .build();
    }

    private ResponseTrxStatusDto buildResponseTrxClientOrAtm(String reference) {
        return ResponseTrxStatusDto.builder()
                .reference(reference)
                .status(StatusEnum.SETTLED.name())
                .amount(new BigDecimal(190.20))
                .fee(new BigDecimal(190.20))
                .build();
    }

    private ResponseTrxStatusDto buildResponseTrxValidateInternal(Optional<Transaction> entity) {
        return ResponseTrxStatusDto.builder()
                .reference(entity.get().getReference())
                .status(StatusEnum.SETTLED.name())
                .amount(new BigDecimal(193.38))
                .fee(new BigDecimal(3.18))
                .date(entity.get().getDate())
                .build();
    }

    private ResponseTrxStatusDto buildResponseTrxValidateATM(String reference) {
        return ResponseTrxStatusDto.builder()
                .reference(reference)
                .status(StatusEnum.PENDING.name())
                .amount(new BigDecimal(190.20))
                .date(LocalDateTime.now())
                .build();
    }

    private ResponseTrxStatusDto buildResponseTrxInternal(String reference) {
        return ResponseTrxStatusDto.builder()
                .reference(reference)
                .status(StatusEnum.PENDING.name())
                .amount(new BigDecimal(190.20))
                .date(LocalDateTime.now())
                .build();
    }

    private ResponseTrxStatusDto buildResponseTrxValidaFuture(Optional<Transaction> entity) {
        return ResponseTrxStatusDto.builder()
				.reference(entity.get().getReference())
				.status(StatusEnum.FUTURE.name())
				.date(entity.get().getDate())
				.amount(new BigDecimal(190.20))
				.build();
    }
}
