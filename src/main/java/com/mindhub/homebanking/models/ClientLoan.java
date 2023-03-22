package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Double amount;
    private Integer payments;
    private String name;

    private Double percentage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client clientID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loanID;
    public ClientLoan() {}
    public ClientLoan(Loan loan, Integer payments, Double amount, Client client){
        this.amount = amount;
        this.payments = payments;
        this.clientID = client;
        this.loanID = loan;
        this.name = loan.getName();
        this.percentage = loan.getPercentage();
    }

//    getters
    public Long getId(){ return id; }
    public Long getLoanID(){ return loanID.getId(); }
    public Long clientID(){ return clientID.getId(); }

    public Double getAmount(){ return amount; }
    public Integer getPayments(){ return payments; }

    public Double getPercentage(){ return percentage; }

    public String getName(){ return name; }

//    setters
    public void setClientID(Client client){ this.clientID = client; }
    public void setLoanID(Loan loan){ this.loanID = loan; }

    public void setAmount(Double amount){ this.amount = amount; }

    public void setPercentage(Double percentage){ this.percentage = percentage; }
    public void setPayments(Integer payments){ this.payments = payments; }
}