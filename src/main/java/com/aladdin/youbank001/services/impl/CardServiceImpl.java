package com.aladdin.youbank001.services.impl;

import com.aladdin.youbank001.configurations.mappers.CardMapper;
import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Customer;
import com.aladdin.youbank001.dao.repositories.CardRepository;
import com.aladdin.youbank001.exceptions.ResourceNotFoundException;
import com.aladdin.youbank001.mail.EmailServiceImpl;
import com.aladdin.youbank001.mail.EmailTemplate;
import com.aladdin.youbank001.model.dtos.response.cards.CardResponseDto;
import com.aladdin.youbank001.model.enums.CardNetwork;
import com.aladdin.youbank001.model.enums.CardType;
import com.aladdin.youbank001.model.enums.Status;
import com.aladdin.youbank001.services.interfaces.CardService;
import com.aladdin.youbank001.services.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {


    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final CustomerService customerServiceImpl;
    private final EmailServiceImpl emailService;

    private final int premiumCardPayment = 3;
    private final int blackPremiumCardPayment = 10;
    private final int ultimateCardPayment = 45;


    @Override
    public CardResponseDto createCard(String customerId, CardType cardType, CardNetwork cardNetwork) {
        Customer customer = customerServiceImpl.findCustomerById(customerId);

        Card card = Card.builder()
                .cardNetwork(cardNetwork)
                .cardType(cardType)
                .account(customer.getAccount())
                .build();

        cardRepository.save(card);
        subscriptionFee(card);
        return cardMapper.toResponse(card);
    }


    @Override
    public void setPinCode(String cardNumber, String pin) {
        Card card = getCardByCardNumber(cardNumber);
        if (card.getPin() == null) {
            card.setPin(pin);
            cardRepository.save(card);
        } else {
            throw new IllegalArgumentException("You already have a pin. Reset pin for a new pin");
        }
    }

    @Override
    public void resetPinCode(String cardNumber, String oldPin, String newPin) {
        Card card = getCardByCardNumber(cardNumber);
        if (card.getPin().equals(oldPin)) {
            card.setPin(newPin);
            cardRepository.save(card);
        } else {
            throw new IllegalArgumentException("Old PIN incorrect!");
        }
    }

    @Override
    public void renameCard(String cardId, String cardName) {
        Card card = getCardById(cardId);
        card.setCardName(cardName);
        cardRepository.save(card);
    }

    @Override
    public List<CardResponseDto> getCustomerCards(String customerId) {
        Customer customer = customerServiceImpl.findCustomerById(customerId);
        List<Card> cards = customer.getAccount().getCards();
        return cardMapper.toResponse(cards);
    }

    @Override
    public BigDecimal getCardBalance(String cardId) {
        Card card = getCardById(cardId);
        return card.getBalance();
    }

    @Override
    public void blockCard(String customerId, String cardId) {
        Customer customer = customerServiceImpl.findCustomerById(customerId);
        Card card = getCardById(cardId);
        checkCardCustomer(customer, card);
        card.setCardStatus(Status.INACTIVE);
        cardRepository.save(card);
    }

    @Override
    public void unblockCard(String customerId, String cardId) {
        Customer customer = customerServiceImpl.findCustomerById(customerId);
        Card card = getCardById(cardId);
        checkCardCustomer(customer, card);
        card.setCardStatus(Status.ACTIVE);
        cardRepository.save(card);
    }


    private void subscriptionFee(Card card) {
        switch (card.getCardType()) {
            case PREMIUM -> {
                BigDecimal subtract = card.getBalance().subtract(BigDecimal.valueOf(premiumCardPayment));
                card.setBalance(subtract);
            }

            case BLACK_PREMIUM -> {
                BigDecimal subtract = card.getBalance().subtract(BigDecimal.valueOf(blackPremiumCardPayment));
                card.setBalance(subtract);
            }
            case ULTIMATE -> {
                BigDecimal subtract = card.getBalance().subtract(BigDecimal.valueOf(ultimateCardPayment));
                card.setBalance(subtract);
            }
        }
        cardRepository.save(card);
    }

    @Scheduled(fixedRate = 2678400000L)
    @Transactional
    public void monthlySubscriptionFee() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Card> cards = cardRepository.findAllByCreatedAt(oneMonthAgo);
        cards.forEach(card -> {
            switch (card.getCardType()) {
                case PREMIUM -> {
                    BigDecimal subtract = card.getBalance().subtract(BigDecimal.valueOf(premiumCardPayment));
                    card.setBalance(subtract);
                }
                case BLACK_PREMIUM -> {
                    BigDecimal subtract = card.getBalance().subtract(BigDecimal.valueOf(blackPremiumCardPayment));
                    card.setBalance(subtract);
                }
                case ULTIMATE -> {
                    BigDecimal subtract = card.getBalance().subtract(BigDecimal.valueOf(ultimateCardPayment));
                    card.setBalance(subtract);
                }
            }
            cardRepository.save(card);
        });
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkCustomerCardsBalance() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Card> cards = cardRepository.findAllByCreatedAt(oneMonthAgo);
        cards.forEach(card -> {
            if (card.getBalance().compareTo(BigDecimal.valueOf(-9)) == 0) {
                Customer customer = getCustomer(card);
                Map<String, String> placeholders =
                        Map.of("userName", customer.getFullName(),
                                "cardNumber", card.getCardNumber().substring(10));
                emailService.sendSimpleEmail(customer.getEmail(), EmailTemplate.BALANCE_INFO, placeholders);
            }
        });
    }

    //   @Cacheable(value = "cardsCache", key = "#cardId")
    public Card getCardById(String cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("CARD_NOT_FOUND"));
    }

    public Card getCardByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new ResourceNotFoundException("CARD_NOT_FOUND"));
    }

    //  @CacheEvict(value = "cards", key = "#cardNumber")

    public void deleteCard(String cardNumber) {
        cardRepository.deleteByCardNumber(cardNumber);
    }

    private void checkCardCustomer(Customer customer, Card card) {
        List<Card> cards = cardRepository.findCardByAccount_Customer(customer);
        if (cards.isEmpty()) {
            throw new IllegalArgumentException(String.format("This - %s user does not have a card", customer.getFullName()));
        }
        boolean contains = cards.contains(card);
        if (contains) {
            throw new IllegalArgumentException(String.format("This card does not belong to %s", customer.getFullName()));
        }
    }

    private Customer getCustomer(Card card) {
        return card.getAccount().getCustomer();
    }

}



