package com.example.bankingapi.service;

import com.example.bankingapi.dto.CreateAccountRequest;
import com.example.bankingapi.model.Account;
import com.example.bankingapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(CreateAccountRequest request) {
        if (request.getPin() == null || request.getPin().length() != 4 || !request.getPin().matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN должен быть из 4х цифр!");
        }
        Account account = new Account();
        account.setOwner(request.getOwner());
        account.setPin(request.getPin());
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Account not found"));
    }
}
