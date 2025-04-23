package com.aladdin.youbank001.model.dtos.response.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseTransactionDto {

    private String transactionId;

    private BigDecimal amount;

    private BigDecimal commission;

    private String transactionDate;

    private String description;

    private String card;

}
