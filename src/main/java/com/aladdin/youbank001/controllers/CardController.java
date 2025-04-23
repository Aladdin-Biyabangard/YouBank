package com.aladdin.youbank001.controllers;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.model.dtos.response.cards.CardResponseDto;
import com.aladdin.youbank001.model.enums.CardNetwork;
import com.aladdin.youbank001.model.enums.CardType;
import com.aladdin.youbank001.services.interfaces.CardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardServiceImpl;
    private final ModelMapper modelMapper;

    @PostMapping(path = "/new")
    public ResponseEntity<CardResponseDto> createCard(@RequestParam String customerId,
                                                      @RequestParam CardType cardType,
                                                      @RequestParam CardNetwork cardNetwork) {
        CardResponseDto card = cardServiceImpl.createCard(customerId, cardType, cardNetwork);
        return ResponseEntity.ok(card);
    }


    @PostMapping(path = "/rename")
    public ResponseEntity<String> renameCard(@RequestParam String cardId, @RequestParam String cardName) {
        cardServiceImpl.renameCard(cardId, cardName);
        return ResponseEntity.ok("Card name changed successfully!");
    }

    @GetMapping(path = "customer")
    public ResponseEntity<List<CardResponseDto>> getCustomerCards(@RequestParam String customerId) {
        List<CardResponseDto> customerCards = cardServiceImpl.getCustomerCards(customerId);
        return ResponseEntity.ok(customerCards);
    }

    @GetMapping(path = "info")
    public ResponseEntity<CardResponseDto> getCardById(@RequestParam String cardId) {
        Card card = cardServiceImpl.getCardById(cardId);
        CardResponseDto cardDto = modelMapper.map(card, CardResponseDto.class);
        return ResponseEntity.ok(cardDto);
    }

    @GetMapping(path = "info-balance")
    public ResponseEntity<BigDecimal> getCardBalance(@RequestParam String cardId) {
        BigDecimal cardBalance = cardServiceImpl.getCardBalance(cardId);
        return ResponseEntity.ok(cardBalance);
    }

    @GetMapping(path = "inactive")
    public ResponseEntity<String> blockCard(@RequestParam String customerId, @RequestParam String cardId) {
        cardServiceImpl.blockCard(customerId, cardId);
        return ResponseEntity.ok("Card successfully blocked: " + cardId);
    }

    @GetMapping(path = "active")
    public ResponseEntity<String> unblock(@RequestParam String customerId, @RequestParam String cardId) {
        cardServiceImpl.unblockCard(customerId, cardId);
        return ResponseEntity.ok("Card successfully blocked: " + cardId);
    }

    public ResponseEntity<String> deleteCard(String cardNumber) {
        cardServiceImpl.deleteCard(cardNumber);
        return ResponseEntity.ok("Card successfully deleted!");
    }
}

