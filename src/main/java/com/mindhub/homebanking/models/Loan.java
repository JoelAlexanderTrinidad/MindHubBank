package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private Integer maxAmount;
    private Double percentage;
    @ElementCollection
    @Column(name = "payment")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loanID", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    public Loan(){}

    public Loan(String name, Integer maxAmount, Double percentage){
        this.name = name;
        this.maxAmount = maxAmount;
        this.percentage = percentage;
    }

    public void addClient(ClientLoan clientLoan){
        clientLoan.setLoanID(this);
        clientLoans.add(clientLoan);
    }

    public Long getId(){ return id; }

    public String getName(){ return name; }
    public Integer getMaxAmount(){ return maxAmount; }
    public Double getPercentage(){ return percentage; }
    public List<Integer> getPayments() { return payments; }

    public Set<ClientLoan> getClients() { return clientLoans; }


    public void setName(String name){ this.name = name; }

    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public void setMaxAmount(Integer maxAmount){ this.maxAmount = maxAmount; }
    public void setPayments(List<Integer> payments) { this.payments = payments; }
}
