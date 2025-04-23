package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Transaction;
import com.aladdin.youbank001.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> getTransactionByCardOrderByTransactionDateDesc(Card card);

    Optional<Transaction> getTransactionByTransactionId(String transactionId);

    List<Transaction> findTransactionsByCardAndTypeAndTransactionDateBetween(Card card, TransactionType type, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = """
    SELECT t1.*
    FROM transaction t1
    JOIN (
        SELECT card_id, MAX(transaction_date) AS max_date
        FROM transaction
        GROUP BY card_id
    ) t2 ON t1.card_id = t2.card_id AND t1.transaction_date = t2.max_date
    ORDER BY t1.transaction_date DESC
    LIMIT 3
    """, nativeQuery = true)
    List<Transaction> findTop3LatestTransactionsByDistinctCard();


}
