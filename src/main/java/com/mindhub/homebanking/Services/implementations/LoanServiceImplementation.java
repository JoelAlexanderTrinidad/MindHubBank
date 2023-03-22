package com.mindhub.homebanking.Services.implementations;

import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoanServiceImplementation implements LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Override
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }
}
