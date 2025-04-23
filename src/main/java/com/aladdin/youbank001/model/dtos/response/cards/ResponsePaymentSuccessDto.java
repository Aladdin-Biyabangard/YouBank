package com.aladdin.youbank001.model.dtos.response.cards;

import com.aladdin.youbank001.model.enums.Status;
import com.aladdin.youbank001.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponsePaymentSuccessDto {

    TransactionType transactionType;
    Status status;
    String transactionInformation;
    BigDecimal amount;

}
