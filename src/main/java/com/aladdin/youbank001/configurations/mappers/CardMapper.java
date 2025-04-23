package com.aladdin.youbank001.configurations.mappers;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.model.dtos.response.cards.CardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CardMapper {


    public CardResponseDto toResponse(Card card) {
        return new CardResponseDto(
                card.getCardNumber(),
                card.getCardName(),
                card.getBalance(),
                card.getCardType(),
                card.getCardNetwork(),
                card.getCardStatus(),
                card.getCreatedAt(),
                card.getCVV()
        );
    }

    public List<CardResponseDto> toResponse(List<Card> cards) {
        return cards.stream().map(this::toResponse).toList();
    }
}
