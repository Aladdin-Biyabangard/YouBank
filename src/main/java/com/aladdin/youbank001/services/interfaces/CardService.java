package com.aladdin.youbank001.services.interfaces;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.model.dtos.response.cards.CardResponseDto;
import com.aladdin.youbank001.model.enums.CardNetwork;
import com.aladdin.youbank001.model.enums.CardType;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {

    CardResponseDto createCard(String customerId, CardType cardType, CardNetwork cardNetwork);

    void setPinCode(String cardNumber, String pin);

    void resetPinCode(String cardNumber, String oldPin, String newPin);

    void renameCard(String cardId, String cardName);

    Card getCardByCardNumber(String cardNumber);

    List<CardResponseDto> getCustomerCards(String customerId);

    Card getCardById(String cardId);

    BigDecimal getCardBalance(String cardId);

    void blockCard(String customerId, String cardId);

    void unblockCard(String customerId, String cardId);

    void deleteCard(String cardNumber);

}
