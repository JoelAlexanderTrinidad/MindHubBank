package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy="client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "clientID", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() { }

    public Client(String first, String last, String email, String password) {
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.password = password;
    }

    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }
    public void addLoan(ClientLoan clientLoan){
        clientLoan.setClientID(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }


    public long getId() {
        return id;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){ return password; }
    public String getLastName(){
        return lastName;
    }
    public String getFirstName(){
        return firstName;
    }
    public Set<Card> getCards(){ return cards; }
    public Set<Account> getAccounts(){
        return accounts;
    }
    public Set<ClientLoan> getLoans() {
        return clientLoans;
    }


    public void setEmail(String email){ this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String toString(){
        return id + " " + firstName + " " + lastName + " " + email;
    }
}
