package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    void save(Transaction transaction);

    List<Transaction> findAll();

}
