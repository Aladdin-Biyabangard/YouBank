package com.aladdin.youbank001.services.interfaces;

import com.aladdin.youbank001.model.dtos.response.cards.CardResponseDto;
import com.aladdin.youbank001.model.dtos.response.cards.ResponsePaymentSuccessDto;
import com.aladdin.youbank001.model.enums.AtmCode;
import com.aladdin.youbank001.model.enums.PaymentType;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    ResponsePaymentSuccessDto transferToAnyBankCard(String fromCard, String toCard, BigDecimal amount);

    ResponsePaymentSuccessDto withdraw(AtmCode atmCode, String cardNumber, BigDecimal amount);

    ResponsePaymentSuccessDto deposit(AtmCode atmCode, String cardNumber, BigDecimal amount);

    ResponsePaymentSuccessDto mobileOperator(String phoneNumber, String cardNumber, BigDecimal amount);

    ResponsePaymentSuccessDto payment(String cardId, PaymentType paymentType, BigDecimal amount);

    List<CardResponseDto> lastTransactionCards();
}
