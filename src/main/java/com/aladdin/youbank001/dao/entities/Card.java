package com.aladdin.youbank001.dao.entities;

import com.aladdin.youbank001.model.enums.CardNetwork;
import com.aladdin.youbank001.model.enums.CardType;
import com.aladdin.youbank001.model.enums.Status;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Card {

    private static final SecureRandom random = new SecureRandom();

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    private String cardNumber;

    private String cardName;

    private BigDecimal balance;

    private String pin;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private CardNetwork cardNetwork;

    @Enumerated(EnumType.STRING)
    private Status cardStatus;

    private LocalDate createdAt;

    private String CVV;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "card")
    @JsonBackReference
    private List<Payment> payments;

    @OneToMany(mappedBy = "card")
    @JsonBackReference
    private List<Transaction> transactions;


    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
        }
        if (this.cardNumber == null) {
            this.cardNumber = generateCardNumber();
        }
        if (this.CVV == null) {
            this.CVV = generateCvv();
        }
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now().plusYears(4);
        }
        if (this.cardStatus == null) {
            this.cardStatus = Status.CREATED;
        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                ", cardType=" + cardType +
                ", cardNetwork=" + cardNetwork +
                ", cardStatus=" + cardStatus +
                ", createdAt=" + createdAt +
                '}';
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();

        cardNumber.append("2605");

        while (cardNumber.length() < 16) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        return cardNumber
                .toString()
                .replaceAll("(.{4})(?!$)", "$1 ");
    }

    public String generateCvv() {
        SecureRandom random = new SecureRandom();
        int cvv = random.nextInt(900) + 100;
        return String.valueOf(cvv);
    }
}
