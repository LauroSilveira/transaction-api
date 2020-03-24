package com.transaction.bank.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "url")
@Component
public class ConfigProperties {

    private String transactionStatus;
}
