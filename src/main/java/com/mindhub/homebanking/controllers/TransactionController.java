package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam(required = false) Double amount, @RequestParam String description,
            @RequestParam String sourceAccount, @RequestParam String destinationAccount,
            Authentication authentication
    ){

        String email = authentication.getName();
        Client client = clientService.findByEmail(email);
        Set<Account> accounts = client.getAccounts();

//      verifico si la cuenta (del cliente autenticado) que se debita existe
        Account sourceAcc = accounts.stream().filter(account -> account.getNumber().equals(sourceAccount)).findFirst().orElse(null);
        Account destinationAcc = accountService.findByNumber(destinationAccount);

        String errors = "";

        if(sourceAcc == null){
            errors += "the source account does not exist";
        }
        if(destinationAcc == null){
            errors += "-the destination account does not exist";
        }
        if(sourceAcc != null  && sourceAcc.equals(destinationAcc)) {
            errors += "-accounts are the same";
        }
        if (amount == null) {
            errors += "-must enter an amount";
        }
        if (amount != null && amount <= 0) {
            errors += "-cannot enter a negative amount";
        }
        if (sourceAccount.isEmpty()){
            errors += "-source account is missing";
        }
        if (destinationAccount.isEmpty()) {
            errors += "-destination account is missing";
        }
        if (description.isEmpty()) {
            errors += "-description is missing";
        }

        if(errors.equals("")){
            for(Account account : accounts) {  // recorro el set de las cuentas del cliente autenticado
                if (account.getNumber().equals(sourceAccount)) { // si es la cuenta de origen seleccionada
                    if(account.getBalance() < 1){  // si esa cuenta seleccionada no tiene saldo, retorname el status code 400
                        return new ResponseEntity<>("You have no available balance",HttpStatus.BAD_REQUEST);
                    }else{

                        if(account.getBalance() < amount){ // si el balance de la cuenta seleccionada es menor al amount que viene por parámetro, 400
                            return new ResponseEntity<>("The amount cannot be less than the account balance",HttpStatus.BAD_REQUEST);
                        }else{ // si no, crea la transacción


                            String debitMsj = "DEBIT - " + sourceAcc.getNumber() + " transferred $" + amount + " to " + destinationAcc.getNumber();
                            String creditMsj = "CREDIT - " + destinationAcc.getNumber() + " received $" + amount + " from " + sourceAcc.getNumber();

                            Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amount, description + ": " + debitMsj, LocalDateTime.now(), sourceAcc.getBalance() - amount, false);
                            Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, description + ": " + creditMsj, LocalDateTime.now(), destinationAcc.getBalance() + amount, false);

                            account.addTransaction(transactionDebit);
                            destinationAcc.addTransaction(transactionCredit);

                            account.setBalance(account.getBalance() - amount);
                            destinationAcc.setBalance(destinationAcc.getBalance() + amount);

                            transactionService.save(transactionDebit);
                            transactionService.save(transactionCredit);

                            accountService.save(account);
                            accountService.save(destinationAcc);

                            return new ResponseEntity<>("transfer created successfully",HttpStatus.CREATED);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }


}
