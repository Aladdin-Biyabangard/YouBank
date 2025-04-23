package com.aladdin.youbank001.dao.entities;

import com.aladdin.youbank001.model.enums.Status;
import com.aladdin.youbank001.model.enums.TransactionType;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private static final SecureRandom random = new SecureRandom();

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    private String transactionId;

    @Column(nullable = false)
    private BigDecimal amount;

    private BigDecimal commission;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JsonIgnore
    private Card card;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
        }
        if (this.transactionId == null) {
            this.transactionId = generateTransactionId();
        }
    }

    private String generateTransactionId() {
        StringBuilder cardNumber = new StringBuilder();

        cardNumber.append("TXN-");
        while (cardNumber.length() < 15) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }
}
