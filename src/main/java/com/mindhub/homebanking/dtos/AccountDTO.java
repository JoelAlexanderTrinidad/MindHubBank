package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class AccountDTO {
    private long id;
    private String number;
    private Double balance;
    private LocalDateTime creationDate;
    private Boolean eliminated;

    private AccountType accountType;
    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(){}
    public AccountDTO(Account account){
        this.id = account.getId();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.number = account.getNumber();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.eliminated = account.getEliminated();
        this.accountType = account.getAccountType();
    }

    public Long getId(){
        return id;
    }

    public String getNumber(){ return number; }
    public Double getBalance(){
        return balance;
    }

    public AccountType getAccountType() { return accountType; }
    public Boolean getEliminated() { return eliminated; }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    public Set<TransactionDTO> getTransactions(){ return transactions; }

}