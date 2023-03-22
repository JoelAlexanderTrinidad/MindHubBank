package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Card;

import java.util.List;

public interface CardService {
    void save(Card card);

    void deleteById(Long id);

    List<Card> findAll();
}
