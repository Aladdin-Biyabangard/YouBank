package com.aladdin.youbank001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Account {

    private static final SecureRandom random = new SecureRandom();


    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccessType accountType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "account")
    private List<Card> cards;

    @OneToMany(mappedBy = "account")
    private List<Loan> loans = new ArrayList<>();

    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
        }
        if (this.accountNumber == null) {
            this.accountNumber = generateAccountNumber();
        }
    }

    private String generateAccountNumber() {
        StringBuilder cardNumber = new StringBuilder();

        cardNumber.append("AZaa2605");

        while (cardNumber.length() < 28) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }
}
