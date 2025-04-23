package com.aladdin.youbank001.services.impl;

import com.aladdin.youbank001.configurations.mappers.CardMapper;
import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Transaction;
import com.aladdin.youbank001.dao.repositories.CardRepository;
import com.aladdin.youbank001.dao.repositories.TransactionRepository;
import com.aladdin.youbank001.exceptions.InsufficientBalanceException;
import com.aladdin.youbank001.model.dtos.response.cards.CardResponseDto;
import com.aladdin.youbank001.model.dtos.response.cards.ResponsePaymentSuccessDto;
import com.aladdin.youbank001.model.enums.AtmCode;
import com.aladdin.youbank001.model.enums.PaymentType;
import com.aladdin.youbank001.model.enums.Status;
import com.aladdin.youbank001.model.enums.TransactionType;
import com.aladdin.youbank001.services.interfaces.CardService;
import com.aladdin.youbank001.services.interfaces.PaymentService;
import com.aladdin.youbank001.services.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CardService cardServiceImpl;
    private final CardRepository cardRepository;
    private final TransactionService transactionServiceImpl;
    private final TransactionRepository transactionRepository;
    private final CardMapper cardMapper;


    @Override
    @Transactional
    public ResponsePaymentSuccessDto transferToAnyBankCard(String fromCard, String toCard, BigDecimal amount) {
        Card fromByCard = cardServiceImpl.getCardByCardNumber(fromCard);
        Optional<Card> cardByNumber = getCardByNumber(toCard);
        BigDecimal commission = appointACommissionWeb(toCard, amount);
        validateSufficientBalance(fromByCard, amount.add(commission));

        if (cardByNumber.isPresent()) {
            Card toByCard = cardByNumber.get();
            toByCard.setBalance(toByCard.getBalance().add(amount));
            cardRepository.save(toByCard);
            String toDescription = "Balance increase • " + fromCard.substring(12).replaceAll(" ", "");
            transactionServiceImpl.createTransactionTransfer(toByCard, amount, TransactionType.TRANSFER, toDescription, BigDecimal.ZERO);
        } else {
            validateCardPrefix(toCard);
        }

        fromByCard.setBalance(fromByCard.getBalance().subtract(amount.add(commission)));
        cardRepository.save(fromByCard);

        String description = "Transfer recipient • " + toCard.substring(12).replaceAll(" ", "");
        Transaction transaction = transactionServiceImpl.createTransactionTransfer(fromByCard, amount, TransactionType.TRANSFER, description, commission);
        return transactionInformation(transaction.getType(), transaction.getDescription(), amount);
    }

    @Transactional
    @Override
    public ResponsePaymentSuccessDto withdraw(AtmCode atmCode, String cardNumber, BigDecimal amount) {
        Card card = cardServiceImpl.getCardByCardNumber(cardNumber);
        BigDecimal commission = appointACommissionAtm(atmCode, amount);
        card.setBalance(card.getBalance().subtract(amount.add(commission)));
        validateSufficientBalance(card, amount);
        cardRepository.save(card);
        Transaction transaction = transactionServiceImpl.createTransactionAtm(card, amount, TransactionType.WITHDRAWAL, commission);
        return transactionInformation(transaction.getType(), transaction.getDescription(), amount);
    }

    @Transactional
    @Override
    public ResponsePaymentSuccessDto deposit(AtmCode atmCode, String cardNumber, BigDecimal amount) {
        Card card = cardServiceImpl.getCardByCardNumber(cardNumber);
        BigDecimal commission = appointACommissionAtm(atmCode, amount);
        card.setBalance(card.getBalance().add(amount).subtract(commission));
        cardRepository.save(card);
        Transaction transaction = transactionServiceImpl.createTransactionAtm(card, amount, TransactionType.DEPOSIT, commission);
        return transactionInformation(transaction.getType(), transaction.getDescription(), amount);
    }

    @Override
    public ResponsePaymentSuccessDto mobileOperator(String phoneNumber, String cardId, BigDecimal amount) {
        String operator = checkPhoneNumber(phoneNumber);
        Card card = cardServiceImpl.getCardById(cardId);
        validateSufficientBalance(card, amount);
        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);
        Transaction transaction = transactionServiceImpl.createTransactionOnline(card, amount, TransactionType.MOBIL, operator + " • " + phoneNumber);
        return transactionInformation(transaction.getType(), transaction.getDescription(), amount);
    }

    @Override
    public ResponsePaymentSuccessDto payment(String cardId, PaymentType paymentType, BigDecimal amount) {
        Card card = cardServiceImpl.getCardById(cardId);
        validateSufficientBalance(card, amount);
        Transaction transaction = transactionServiceImpl.createTransactionPayment(card, amount, TransactionType.PAYMENT, paymentType);
        return transactionInformation(TransactionType.PAYMENT, transaction.getDescription(), amount);
    }

    @Override
    public List<CardResponseDto> lastTransactionCards() {
        List<Transaction> last3Transactions = transactionRepository.findTop3LatestTransactionsByDistinctCard();
        List<Card> cards = last3Transactions.stream().map(Transaction::getCard).toList();
        return cardMapper.toResponse(cards);
    }

    private void validateSufficientBalance(Card fromCard, BigDecimal amount) {
        if (amount.compareTo(fromCard.getBalance()) >= 0) {
            throw new InsufficientBalanceException();
        }
    }

    private BigDecimal appointACommissionWeb(String cardNumber, BigDecimal amount) {
        if (!cardNumber.startsWith("2605")) {
            return amount.multiply(BigDecimal.valueOf(0.01));
        }
        return BigDecimal.ZERO;
    }

    private Optional<Card> getCardByNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    private BigDecimal appointACommissionAtm(AtmCode atmCode, BigDecimal amount) {
        if (!atmCode.getCode().equals("A2605")) {
            return amount.multiply(BigDecimal.valueOf(0.01));
        }
        return BigDecimal.ZERO;
    }

    private String checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 9) {
            throw new IllegalArgumentException("Phone number incorrect. Must be 9 digits!");
        }
        String prefix = phoneNumber.substring(0, 2);
        return switch (prefix) {
            case "50", "51", "10" -> "Azercell";
            case "77", "70" -> "NarMobile";
            case "99", "55" -> "Bakcell";
            default -> throw new IllegalArgumentException("There is no such mobile provider.");
        };
    }

    private void validateCardPrefix(String cardNumber) {
        if (cardNumber.length() == 19) {
            if (cardNumber.startsWith("2605") || cardNumber.startsWith("4169") || cardNumber.startsWith("2363") || cardNumber.startsWith("9940")) {
            } else {
                throw new IllegalArgumentException("Card prefix is incorrect.");
            }
        } else {
            throw new IllegalArgumentException("Card number length incorrect.");
        }
    }

    private ResponsePaymentSuccessDto transactionInformation(TransactionType transactionType, String description, BigDecimal amount) {
        return new ResponsePaymentSuccessDto(
                transactionType,
                Status.SUCCESSFULLY,
                description,
                amount
        );
    }
}