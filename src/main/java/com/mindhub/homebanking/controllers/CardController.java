package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam String cardType,
            @RequestParam String cardColor, Authentication authentication
    ){

        String errors = "";

        if(cardColor.isEmpty() || cardColor.equals("Select a color...")){
            errors += "you have choose a color of card";
        }
        if(cardType.isEmpty() || cardType.equals("Select a type...")){
            errors += "-you have choose a type of card";
        }

        String email = authentication.getName();
        Client client = clientService.findByEmail(email);

        Set<Card> cards = client.getCards();
        Set<String> cardStrings = cards.stream()
                .map(card -> card.getType() + " " + card.getColor()) // combinar tipo y color en un solo string
                .collect(Collectors.toSet());
        // valido si ya posee la cards que eligió en el select
        for(String card : cardStrings) {
            if (cardType.equals(card.split(" ")[0]) && cardColor.equals(card.split(" ")[1])) {
                return new ResponseEntity<>("You already have a card of that type",HttpStatus.BAD_REQUEST);
            }
        }

        if(errors.equals("")){
            LocalDateTime from = LocalDateTime.now();
            LocalDateTime thru = from.plus(Period.ofYears(5));
            short ccvNumber = CardUtils.getCcvNumber();
            String cardNumber = CardUtils.getCardNumber();

            CardType cardTypeEnum = CardType.valueOf(cardType);
            CardColor cardColorEnum = CardColor.valueOf(cardColor);
            Card card = new Card(client.getFirstName() + " " + client.getLastName(), cardColorEnum, cardNumber, cardTypeEnum , ccvNumber, from, thru);
            client.addCard(card);
            cardService.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);

    }



    @PostMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id){

        cardService.deleteById(id);
        return ResponseEntity.ok().build(); // .build() se utiliza para construir una instancia de ResponseEntity devolver una respuesta HTTP con un código de estado 200 y sin cuerpo
    }

    @PostMapping("/clients/current/cards/payments")
    public ResponseEntity<Object> createPayment(
            @RequestParam String cardNumber,
            @RequestParam String cvv,
            @RequestParam String amount,
            @RequestParam String description,
            Authentication authentication
    ){
        String errors = "";

        String email = authentication.getName();
        Client client = clientService.findByEmail(email);
        Set<Card> cards = client.getCards();

        List<Card> debitCards = cards.stream().filter(card -> card.getType().equals(CardType.DEBIT)).collect(Collectors.toList());

//        for(Card card: debitCards){
//            if(card.getThruDate().isBefore(LocalDateTime.now())){
//                return new ResponseEntity<>(card, HttpStatus.FORBIDDEN);
//            }
//        }


        if(cardNumber == null || cardNumber.equals("")){
            errors += "card number is empty";
        }
        if(cardNumber != null && !cardNumber.matches("[0-9\\-]+")){ // pregunto si no contiene numeros y un guion medio
            errors += "-the card number should only have numbers";
        }
        if(cvv == null || cvv.equals("")){
            errors += "-cvv number is empty";
        }
        if(cvv != null && !cvv.matches("[0-9]+")){
            errors += "-the cvv should only have numbers";
        }
        if(amount.equals("")){
            errors += "-amount is empty";
        }
        if(!amount.matches("[0-9]+")){
            errors += "-the amount should only have numbers";
        }
        if(description.equals("")){
            errors += "-description is empty";
        }

        if(errors.equals("")){

            Short cvvShort = Short.parseShort(cvv);
            Card myCard = cards.stream().filter(card -> card.getNumber().equals(cardNumber) && card.getCvv().equals(cvvShort)).findFirst().orElse(null);
            double amountDouble = Double.parseDouble(amount);

            if(myCard == null){
                return new ResponseEntity<>("card not found, verify that you have entered the numbers correctly", HttpStatus.BAD_REQUEST);
            }
            if(myCard.getThruDate().isBefore(LocalDateTime.now())){
                return new ResponseEntity<>("the card expired", HttpStatus.BAD_REQUEST);
            }

            Set<Account> accounts =  client.getAccounts();
            Account maxAmount = null;

            // busco la cuenta que tenga mas saldo
            for(Account account : accounts){
                if(maxAmount == null || account.getBalance() > maxAmount.getBalance()){
                    maxAmount = account;
                }
            }


            if(maxAmount != null && maxAmount.getBalance() >= amountDouble){
                Double currentAmount = maxAmount.getBalance() - amountDouble;
                Transaction transaction = new Transaction(TransactionType.DEBIT, amountDouble, description, LocalDateTime.now(), currentAmount, false);
                maxAmount.setBalance(maxAmount.getBalance() - amountDouble);
                accountService.save(maxAmount);
                transactionService.save(transaction);
                return new ResponseEntity<>("payment made!", HttpStatus.OK);
            }

            return new ResponseEntity<>("you have no available balance!", HttpStatus.BAD_REQUEST);


        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }






}
