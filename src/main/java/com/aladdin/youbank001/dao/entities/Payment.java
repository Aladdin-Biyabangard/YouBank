package com.aladdin.youbank001.dao.entities;

import com.aladdin.youbank001.model.enums.PaymentType;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    private Double amount;


    private PaymentType paymentType;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne
    @JsonIgnore
    private Card card;

    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
        }
    }
}
