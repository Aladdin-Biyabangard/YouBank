package com.aladdin.youbank001.services.impl;

import com.aladdin.youbank001.configurations.mappers.TransactionMapper;
import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Transaction;
import com.aladdin.youbank001.dao.repositories.TransactionRepository;
import com.aladdin.youbank001.exceptions.ResourceNotFoundException;
import com.aladdin.youbank001.mail.EmailServiceImpl;
import com.aladdin.youbank001.mail.EmailTemplate;
import com.aladdin.youbank001.model.dtos.response.transactions.ResponseTransactionDto;
import com.aladdin.youbank001.model.enums.PaymentType;
import com.aladdin.youbank001.model.enums.Status;
import com.aladdin.youbank001.model.enums.TransactionType;
import com.aladdin.youbank001.services.interfaces.CardService;
import com.aladdin.youbank001.services.interfaces.TransactionService;
import com.aladdin.youbank001.utils.Export;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CardService cardServiceImpl;
    private final Export export;
    private final EmailServiceImpl emailServiceImpl;


    @Override
    public ResponseTransactionDto getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository
                .getTransactionByTransactionId(transactionId).orElseThrow(() -> new ResourceNotFoundException("TRANSACTION_NOT_FOUND"));
        return transactionMapper.toResponse(transaction);
    }

    @Override
    public List<ResponseTransactionDto> getTransactions(String cardNumber) {
        Card card = cardServiceImpl.getCardByCardNumber(cardNumber);
        List<Transaction> transactions = transactionRepository.getTransactionByCardOrderByTransactionDateDesc(card);
        return transactionMapper.toResponse(transactions);
    }

    @Override
    public void paymentStatements(String cardId, String email, TransactionType type, LocalDate startDate, LocalDate endDate) throws InterruptedException {
        Card card = cardServiceImpl.getCardById(cardId);
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atStartOfDay();
        List<Transaction> transactions =
                transactionRepository.findTransactionsByCardAndTypeAndTransactionDateBetween(card, type, startOfDay, endOfDay);
        List<ResponseTransactionDto> transactionsDto = transactionMapper.toResponse(transactions);

        Map<String, String> placeholders = Map.of("start", startDate.toString(),
                "end", endDate.toString());
        File file = export.exportToExcel(transactionsDto, startDate, endDate);
        Thread.sleep(100);
        emailServiceImpl.sendFileEmail(email, EmailTemplate.PAYMENT_STATEMENTS, placeholders, file);
    }

    @Override
    public Transaction createTransactionAtm(Card card, BigDecimal amount, TransactionType transactionType, BigDecimal commission) {

        String description = switch (transactionType) {
            case DEPOSIT -> "Balance increase via ATM";
            case WITHDRAWAL -> "Cash withdrawal via ATM";
            default -> throw new IllegalStateException("Unexpected value: " + transactionType);
        };
        return createTransaction(card, amount, transactionType, description, commission);
    }

    @Override
    public Transaction createTransactionTransfer(Card card, BigDecimal amount, TransactionType transactionType, String description, BigDecimal commission) {
        return createTransaction(card, amount, transactionType, description, commission);
    }

    @Override
    public Transaction createTransactionOnline(Card fromCard, BigDecimal amount, TransactionType transactionType, String mobilOperator) {
        return createTransaction(fromCard, amount, transactionType, mobilOperator, BigDecimal.ZERO);
    }

    @Override
    public Transaction createTransactionPayment(Card fromCard, BigDecimal amount, TransactionType transactionType, PaymentType paymentType) {
        String description =  paymentType + " • " + "payment!";
        return createTransaction(fromCard, amount, transactionType, description, BigDecimal.ZERO);
    }

    private Transaction createTransaction(Card card, BigDecimal amount, TransactionType transactionType, String description, BigDecimal commission) {
        Transaction transaction = Transaction.builder()
                .card(card)
                .amount(amount)
                .status(Status.SUCCESSFULLY)
                .type(transactionType)
                .description(description)
                .commission(commission)
                .build();
        return transactionRepository.save(transaction);
    }
}
