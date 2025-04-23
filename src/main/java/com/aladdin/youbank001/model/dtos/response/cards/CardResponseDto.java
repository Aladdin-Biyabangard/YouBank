package com.aladdin.youbank001.model.dtos.response.cards;

import com.aladdin.youbank001.model.enums.CardNetwork;
import com.aladdin.youbank001.model.enums.CardType;
import com.aladdin.youbank001.model.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDto {

    private String cardNumber;

    private String cardName;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private CardNetwork cardNetwork;

    @Enumerated(EnumType.STRING)
    private Status cardStatus;

    private LocalDate expiryDate;

    private String CVV;
}
