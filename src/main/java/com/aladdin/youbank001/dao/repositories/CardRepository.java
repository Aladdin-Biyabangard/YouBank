package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.Account;
import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Customer;
import com.aladdin.youbank001.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, String> {

    Optional<Card> findByCardNumber(String cardNumber);

    void deleteByCardNumber(String cardNumber);

    List<Card> findCardByAccount_Customer(Customer customer);

    List<Card> findAllByCreatedAt(LocalDate oneMonthAgo);

    List<Card> findCardsByAccountAndCardType(Account account, CardType cardType);
}
