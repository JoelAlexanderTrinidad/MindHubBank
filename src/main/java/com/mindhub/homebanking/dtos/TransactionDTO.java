package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    private Double currentAmount;

    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction){
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.currentAmount = transaction.getCurrentAmount();
    }

    public Long getId(){
        return id;
    }

    public double getAmount(){
        return amount;
    }

    public TransactionType getType(){
        return type;
    }
    public LocalDateTime getDate(){
        return date;
    }

    public String getDescription(){
        return description;
    }

    public Double getCurrentAmount() { return currentAmount; }

}


