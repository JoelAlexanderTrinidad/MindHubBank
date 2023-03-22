package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        Loan loan = loanService.findById(loanApplicationDTO.getLoanID());
        Account accountDestination = accountService.findByNumber(loanApplicationDTO.getDestinationAccount());
        String email = authentication.getName();
        Client client = clientService.findByEmail(email);

        String errors = "";


        if (loanApplicationDTO.getAmount() == null) {
            errors += "missing amount";
        }
        if (loanApplicationDTO.getAmount() != null && loanApplicationDTO.getAmount() <= 0) {
            errors += "-the amount cannot be less than 1";
        }
        if (loanApplicationDTO.getPayments() == null) {
            errors += "-missing payment";
        }
        if (loanApplicationDTO.getPayments() != null && loanApplicationDTO.getPayments() == 0) {
            errors += "-the payment cannot be 0";
        }
        if (loanApplicationDTO.getDestinationAccount().equals("")) {
            errors += "-missing destination account";
        }
        if (loanApplicationDTO.getAmount() != null && loan != null && loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            errors += "-you exceeded the maximum amount";
        }
        if (accountDestination == null) {
            errors += "-destination account not found";
        }
        if (!client.getAccounts().contains(accountDestination)) {
            errors += "-you don't have that account";
        }
        if(errors.equals("")){

            double amountPlusPercentage = (loanApplicationDTO.getAmount() * loan.getPercentage()) + loanApplicationDTO.getAmount();
            ClientLoan clientLoan = new ClientLoan(loan, loanApplicationDTO.getPayments(), amountPlusPercentage, client);
            Double currentAmount = accountDestination.getBalance() + loanApplicationDTO.getAmount();
            Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved!", LocalDateTime.now(), currentAmount, false);
            accountDestination.setBalance(accountDestination.getBalance() + loanApplicationDTO.getAmount());
            accountDestination.addTransaction(transaction);
            client.addLoan(clientLoan);

            transactionService.save(transaction);
            accountService.save(accountDestination);
            clientLoanService.save(clientLoan);

            return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/loans/admin")
    public ResponseEntity<Object> createNewLoan(
            @RequestParam String loanName,
            @RequestParam String maxAmount,
            @RequestParam String payments,
            @RequestParam String percentage
    ){

        String errors = "";

        if(loanName.equals("")){
            errors += "loan name is empty";
        }
        if(maxAmount.equals("")){
            errors += "-max amount is empty";
        }
        if(!maxAmount.matches("[0-9]+")){ // pregunto si no contiene numero
            errors += "-the maximum amount should only have numbers";
        }
        if(payments.equals("")){
            errors += "-payments is empty";
        }
        if(!payments.matches("[0-9\\-]+")){ // pregunto si no contiene numeros y un guion medio
            errors += "-the payments should only have numbers";
        }
        if(percentage.equals("")){
            errors += "-percentage is empty";
        }
        if(!percentage.matches("[0-9]+")){ // pregunto si no contiene numero
            errors += "-the percentage should only have numbers";
        }

        if(errors.equals("")){
            int maxAmountInt = Integer.parseInt(maxAmount);
            Double percentageDouble = Double.parseDouble(percentage);

            String[] listaPays = payments.split("-");
            List<Integer> numberList = new ArrayList<Integer>();
//            int[] numbers = new int[pays.length]; // creamos un nuevo array con la misma longitud de pays

            for (String string : listaPays) {
                int num = Integer.parseInt(string);
                numberList.add(num);
            }

            Loan loan = new Loan(loanName, maxAmountInt,percentageDouble);
            loan.setPayments(numberList);
            loanService.save(loan);

            return new ResponseEntity<>("new loan type created!", HttpStatus.CREATED);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getLoan(){
        return loanService.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

}

