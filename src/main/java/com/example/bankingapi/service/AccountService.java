package com.example.bankingapi.service;

import com.example.bankingapi.dto.CreateAccountRequest;
import com.example.bankingapi.dto.TransactionRequest;
import com.example.bankingapi.dto.TransferRequest;
import com.example.bankingapi.model.Account;
import com.example.bankingapi.model.Transaction;
import com.example.bankingapi.repository.AccountRepository;
import com.example.bankingapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setOwner(request.getOwner());
        account.setPin(request.getPin());
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account deposit(Long id, TransactionRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма депозита должна быть больше нуля");
        }
        Account account = accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Аккаунт не найден"));
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);
        saveTransaction(id, request.getAmount(), "DEPOSIT");
        return account;
    }

    public Account withdraw(Long id, TransactionRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть больше нуля");
        }
        Account account = accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Аккаунт не найден"));
        if (!account.getPin().equals(request.getPin())) {
            throw new IllegalArgumentException("Неправильный PIN");
        }
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счете");
        }
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);
        saveTransaction(id, request.getAmount(), "WITHDRAWAL");
        return account;
    }

    public void transfer(TransferRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        }
        Account sourceAccount = accountRepository.findById(request.getSourceAccountId()).orElseThrow(() -> new NoSuchElementException("Аккаунт отправителя не найден"));
        Account destinationAccount = accountRepository.findById(request.getDestinationAccountId()).orElseThrow(() -> new NoSuchElementException("Аккаунт получателя не найден"));
        if (!sourceAccount.getPin().equals(request.getPin())) {
            throw new IllegalArgumentException("Неправильный PIN");
        }
        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счете отправителя");
        }
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        saveTransaction(request.getSourceAccountId(), request.getAmount(), "TRANSFER OUT");
        saveTransaction(request.getDestinationAccountId(), request.getAmount(), "TRANSFER IN");
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    private void saveTransaction(Long accountId, BigDecimal amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
