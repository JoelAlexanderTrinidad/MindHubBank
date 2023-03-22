package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class LoanApplicationDTO {
    private Long loanID;
    private Double amount;
    private Integer payments;
    private String destinationAccount;

    private Double percentage;

    public LoanApplicationDTO(){}

    public LoanApplicationDTO(ClientLoan clientLoan, String account ){
        this.loanID = clientLoan.getLoanID();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.destinationAccount = account;
        this.percentage = clientLoan.getPercentage();
    }

    public Long getLoanID(){ return loanID; }

    public Double getAmount(){ return amount; }
    public Integer getPayments(){ return payments; }

    public Double getPercentage(){ return percentage; }
    public String getDestinationAccount(){ return destinationAccount; }

}
