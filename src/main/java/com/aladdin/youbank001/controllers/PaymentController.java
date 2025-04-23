package com.aladdin.youbank001.controllers;

import com.aladdin.youbank001.model.dtos.response.cards.CardResponseDto;
import com.aladdin.youbank001.model.dtos.response.cards.ResponsePaymentSuccessDto;
import com.aladdin.youbank001.model.enums.AtmCode;
import com.aladdin.youbank001.model.enums.PaymentType;
import com.aladdin.youbank001.services.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentServiceImpl;


    @PostMapping(path = "transfer")
    public ResponseEntity<ResponsePaymentSuccessDto> cardToCard(@RequestParam String fromCard,
                                                                @RequestParam String toCard,
                                                                @RequestParam BigDecimal amount) {
        ResponsePaymentSuccessDto responsePaymentSuccessDto = paymentServiceImpl.transferToAnyBankCard(fromCard, toCard, amount);
        return ResponseEntity.ok(responsePaymentSuccessDto);
    }

    @PostMapping(path = "withdraw")
    public ResponseEntity<ResponsePaymentSuccessDto> withdraw(@RequestParam AtmCode atmCode,
                                                              @RequestParam String cardNumber,
                                                              @RequestParam BigDecimal amount) {
        ResponsePaymentSuccessDto withdraw = paymentServiceImpl.withdraw(atmCode, cardNumber, amount);
        return ResponseEntity.ok(withdraw);
    }

    @PostMapping(path = "deposit")
    public ResponseEntity<ResponsePaymentSuccessDto> deposit(@RequestParam AtmCode atmCode,
                                                             @RequestParam String cardNumber,
                                                             @RequestParam BigDecimal amount) {
        ResponsePaymentSuccessDto deposit = paymentServiceImpl.deposit(atmCode, cardNumber, amount);
        return ResponseEntity.ok(deposit);
    }

    @PostMapping(path = "mobile")
    public ResponseEntity<ResponsePaymentSuccessDto> mobileOperator(@RequestParam String phoneNumber,
                                                                    @RequestParam String cardId,
                                                                    @RequestParam BigDecimal amount) {
        ResponsePaymentSuccessDto responsePaymentSuccessDto = paymentServiceImpl.mobileOperator(phoneNumber, cardId, amount);
        return ResponseEntity.ok(responsePaymentSuccessDto);
    }

    @PostMapping(path = "{cardId}/payment")
    public ResponseEntity<ResponsePaymentSuccessDto> payment(@PathVariable String cardId,
                                                             @RequestParam PaymentType paymentType,
                                                             @RequestParam BigDecimal amount) {
        ResponsePaymentSuccessDto payment = paymentServiceImpl.payment(cardId, paymentType, amount);
        return ResponseEntity.ok(payment);
    }

    @GetMapping(path = "last")
    public ResponseEntity<List<CardResponseDto>> lastTransactionCards() {
        List<CardResponseDto> cards = paymentServiceImpl.lastTransactionCards();
        return ResponseEntity.ok(cards);
    }

}
