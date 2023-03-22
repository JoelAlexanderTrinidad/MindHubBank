package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loan = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();

    public ClientDTO(){}
    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loan = client.getLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    public Long getId(){
        return id;
    }
    public String getEmail(){
        return email;
    }
    public String getLastName(){
        return lastName;
    }
    public String getFirstName(){
        return firstName;
    }

    public Set<ClientLoanDTO> getLoan(){
        return loan;
    }
    public Set<AccountDTO> getAccounts(){
        return accounts;
    }
    public Set<CardDTO> getCards(){ return cards; }

}
