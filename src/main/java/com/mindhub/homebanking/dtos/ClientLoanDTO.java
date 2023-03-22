package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private Long id;
    private Long loanID;
    private Long clientID;
    private String name;
    private double amount;
    private Integer payments;

    public ClientLoanDTO(){}
    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanID = clientLoan.getLoanID();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.name = clientLoan.getName();
    }

    public Long getId(){ return id; }

    public String getName(){
        return name;
    }
    public Long getLoanID(){ return loanID; }

    public double getAmount(){
        return amount;
    }
    public Integer getPayments(){
        return payments;
    }
}
