package com.aladdin.youbank001.services.interfaces;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Transaction;
import com.aladdin.youbank001.model.dtos.response.transactions.ResponseTransactionDto;
import com.aladdin.youbank001.model.enums.PaymentType;
import com.aladdin.youbank001.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    ResponseTransactionDto getTransactionById(String transactionId);

    void paymentStatements(String cardId, String email, TransactionType type, LocalDate startDate, LocalDate endDate) throws InterruptedException;

    List<ResponseTransactionDto> getTransactions(String cardNumber);

    Transaction createTransactionAtm(Card card, BigDecimal amount, TransactionType transactionType, BigDecimal commission);

    Transaction createTransactionTransfer(Card card, BigDecimal amount, TransactionType transactionType, String description, BigDecimal commission);

    Transaction createTransactionOnline(Card fromCard, BigDecimal amount, TransactionType transactionType, String mobilOperator);

    Transaction createTransactionPayment(Card fromCard, BigDecimal amount, TransactionType transactionType, PaymentType paymentType);
}
