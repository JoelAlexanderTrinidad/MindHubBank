package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    Loan findById(Long id);

    void save(Loan loan);

    List<Loan> findAll();
}
