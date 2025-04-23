package com.aladdin.youbank001.cacheData;

import com.aladdin.youbank001.dao.entities.Account;
import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.repositories.AccountRepository;
import com.aladdin.youbank001.dao.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CacheData {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public volatile Set<Card> cardsCache = new HashSet<>();

    public volatile Set<Account> accountsCache = new HashSet<>();


    @Scheduled(fixedRate = 86400000L)
    public void loadCardCache() {
        List<Card> cards = cardRepository.findAll();
        cardsCache = new HashSet<>(cards);
    }

    @Scheduled(fixedRate = 86400000L)
    public void loadAccountCache() {
        List<Account> accounts = accountRepository.findAll();
        accountsCache = new HashSet<>(accounts);
    }
}
