package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {
    private Long id;
    private Short cvv;
    private CardType type;
    private CardColor color;
    private String cardHolder, number;
    private LocalDateTime thruDate, fromDate;

    public CardDTO(){}

    public CardDTO(Card card){
        this.id = card.getId();
        this.cvv = card.getCvv();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.cardHolder = card.getCardHolder();

    }

    public Long getId(){ return id; }
    public Short getCvv(){ return cvv; }
    public CardType getType(){ return type; }
    public String getNumber(){ return number; }
    public CardColor getColor(){ return color; }
    public String getCardHolder(){ return cardHolder; }
    public LocalDateTime getFromDate(){ return fromDate; }
    public LocalDateTime getThruDate(){ return thruDate; }

}
