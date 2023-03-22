package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;
    private Double currentAmount;

    private Boolean eliminated;

    public Transaction() {}

    public Transaction(TransactionType type, double amount, String description, LocalDateTime date, Double currentAmount, Boolean eliminated){
        if(type == TransactionType.DEBIT){
            this.amount = amount * -1;
        }else{
            this.amount = amount;
        }

        this.type = type;
        this.description = description;
        this.date = date;
        this.currentAmount = currentAmount;
        this.eliminated = eliminated;
    }


    public Long getId(){return id;}

    public double getAmount(){
        return amount;
    }
    public LocalDateTime getDate(){
        return date;
    }

    public TransactionType getType(){
        return type;
    }

    public Account getAccount(){ return account; }

    private Boolean getEliminated() { return eliminated; }

    public String getDescription(){
        return description;
    }
    public Double getCurrentAmount(){return currentAmount;}


    public void setAmount(double amount){
        this.amount = amount;
    }
    public void setDate(LocalDateTime date){
        this.date = date;
    }


    public void setType(TransactionType type){
        this.type = type;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    public void setEliminated(Boolean eliminated) { this.eliminated = eliminated; }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCurrentAmount(Double currentAmount) { this.currentAmount = currentAmount; }

}
