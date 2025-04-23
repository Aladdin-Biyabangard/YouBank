package com.aladdin.youbank001.configurations.mappers;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Transaction;
import com.aladdin.youbank001.model.dtos.response.transactions.ResponseTransactionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TransactionMapper {

    public ResponseTransactionDto toResponse(Transaction transaction) {
        return new ResponseTransactionDto(
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getCommission(),
                formatDate(transaction.getTransactionDate()),
                createDescription(transaction),
                cardInfo(transaction.getCard())
        );
    }


    public List<ResponseTransactionDto> toResponse(List<Transaction> transactions) {
        return transactions.stream().map(this::toResponse).toList();
    }

    private String cardInfo(Card card) {
        String cardNumber = card.getCardNumber();
        String cardType = card.getCardType().toString();
        return cardType + " • " + cardNumber.trim().substring(10);
    }

    private String createDescription(Transaction transaction) {
        String description = transaction.getDescription();
        BigDecimal commission = transaction.getCommission();
        if (commission != null) {
            switch (transaction.getType()) {
                case WITHDRAWAL ->
                        description = transaction.getAmount().add(commission) + " ₼ were deducted from your balance," +
                                " this amount includes a " + commission + " ₼ commission.";
                case DEPOSIT ->
                        description = "Your balance has been increased by " + transaction.getAmount().subtract(commission) + " ₼, " +
                                "this amount includes a " + commission + " ₼ commission ";
            }
        }
        return description;
    }


    private String formatDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        return localDateTime.format(formatter);
    }

}