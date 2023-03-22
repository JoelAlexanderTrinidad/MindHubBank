package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    void save(Account account);

    Account findByNumber(String number);

    List<Account> findAll();

    Account findById(Long id);

}
