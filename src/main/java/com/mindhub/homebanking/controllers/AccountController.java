package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(
            @RequestParam String accountType,
            Authentication authentication
    ){

        String email = authentication.getName();
        Client client = clientService.findByEmail(email);

        if(client == null){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }

        addAccount(client, accountType);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PostMapping("/accounts/delete/{number}")
    ResponseEntity<Object> deleteAccount(@PathVariable String number){

        Account acc = accountService.findByNumber(number);
        acc.setEliminated(true);
        accountService.save(acc);

        Set<Transaction> transactions = acc.getTransactions();

        transactions.forEach(transaction -> {
            transaction.setEliminated(true);
            transactionService.save(transaction);
        });


        return new ResponseEntity<>("Account deleted!", HttpStatus.OK);
    }

    public void addAccount(Client client, String accType){

        AccountType accountType = AccountType.valueOf(accType);

        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String randomNumberStr = String.format("%05d", randomNumber);
        Account account = new Account(LocalDateTime.now(), (double) 0, "VIN"+"-"+randomNumberStr, false, accountType);
        client.addAccount(account);
        accountService.save(account);
    }

    @GetMapping("/accounts/{number}/transaction")
    public Set<TransactionDTO> getTransactions(@PathVariable String number, @RequestParam Date starDate, @RequestParam Date endDate) {
        Account account = accountService.findByNumber(number);
        return account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());
    }


    @GetMapping("/accounts")
    public Set<AccountDTO> getAccounts() {
        return accountService.findAll().stream().map(account -> new AccountDTO(account)).collect(toSet());
    }


    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.findById(id));
    }
}