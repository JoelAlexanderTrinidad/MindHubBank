package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bytebuddy.asm.Advice;
import org.apache.catalina.Cluster;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    private String number;
    private LocalDateTime creationDate;
    private Double balance;
    private Boolean eliminated;

    private AccountType accountType;

    public Account(){}

    public Account(LocalDateTime creationDate, Double balance, String number, Boolean eliminated, AccountType accountType){
        this.creationDate = creationDate;
        this.balance = balance;
        this.number = number;
        this.eliminated = eliminated;
        this.accountType = accountType;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public long getId(){
        return id;
    }

    public String getNumber(){
        return number;
    }
    public double getBalance(){
        return balance;
    }
    public Boolean getEliminated() { return eliminated; }

    public Client getClient(){ return this.client; }
    public AccountType getAccountType() { return this.accountType; }
    public LocalDateTime getCreationDate(){ return creationDate; }

    public Set<Transaction> getTransactions(){ return transactions; }

    public void setNumber(String number){ this.number = number; }

    public void setClient(Client client) { this.client = client; }
    public void setBalance(Double balance){ this.balance = balance; }
    public void setEliminated(Boolean eliminated) { this.eliminated = eliminated; }

    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public void setCreationDate(LocalDateTime creationDate){ this.creationDate = creationDate; }

}
