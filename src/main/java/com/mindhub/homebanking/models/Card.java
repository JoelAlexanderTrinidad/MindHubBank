package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Short cvv;
    private CardType type;
    private CardColor color;
    private String cardHolder, number;
    private LocalDateTime thruDate, fromDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card(){}
    public Card(String cardHolder, CardColor color, String number, CardType type, Short cvv, LocalDateTime fromDate, LocalDateTime thruDate){
        this.cardHolder = cardHolder;
        this.color = color;
        this.number = number;
        this.type = type;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    public Long getId(){ return id; }
    public Short getCvv(){ return cvv; }

    public CardType getType(){ return type; }

    public String getNumber(){ return number; }

    public CardColor getColor(){ return color; }
    public String getCardHolder(){ return cardHolder; }
    public LocalDateTime getFromDate(){ return fromDate; }
    public LocalDateTime getThruDate(){ return thruDate; }

    public void setCvv(Short cvv){ this.cvv = cvv; }

    public void setType(CardType type){ this.type = type; }

    public void setColor(CardColor color){ this.color = color; }

    public void setClient(Client client){ this.client = client; }

    public void setNumber(String number){ this.number = number; }
    public void setCardHolder(String cardHolder){ this.cardHolder = cardHolder; }
    public void setThruDate(LocalDateTime thruDate){ this.thruDate = thruDate; }
    public void setFromDate(LocalDateTime fromDate){ this.fromDate = fromDate; }
}
