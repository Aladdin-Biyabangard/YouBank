package com.aladdin.youbank001.controllers;

import com.aladdin.youbank001.model.dtos.response.transactions.ResponseTransactionDto;
import com.aladdin.youbank001.model.enums.TransactionType;
import com.aladdin.youbank001.services.interfaces.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionServiceImpl;


    @GetMapping(path = "transaction")
    public ResponseEntity<ResponseTransactionDto> getTransactionById(@RequestParam String transactionId) {
        ResponseTransactionDto transactionById = transactionServiceImpl.getTransactionById(transactionId);
        return ResponseEntity.ok(transactionById);
    }

    @GetMapping("/statements")
    public ResponseEntity<String> getStatements(
            @RequestParam String cardId,
            @RequestParam String email,
            @RequestParam TransactionType type,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws InterruptedException {

        transactionServiceImpl.paymentStatements(cardId, email, type, startDate, endDate);
        return ResponseEntity.ok("Successfully send");
    }

    @GetMapping("/date-check")
    @Operation(summary = "Test for LocalDate picker in Swagger")
    public ResponseEntity<String> checkDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Start date in format yyyy-MM-dd")
            LocalDate startDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "End date in format yyyy-MM-dd")
            LocalDate endDate
    ) {
        return ResponseEntity.ok("Start: " + startDate + ", End: " + endDate);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<ResponseTransactionDto>> getTransactions(@RequestParam String cardNumber) {
        List<ResponseTransactionDto> transactions = transactionServiceImpl.getTransactions(cardNumber);
        return ResponseEntity.ok(transactions);
    }


}
