package com.aladdin.youbank001.model.dtos.request;

import com.aladdin.youbank001.dao.entities.Card;

import java.math.BigDecimal;

public record TransactionDto(BigDecimal amount, String description, Card fromCard, String toCard) {

}
