package com.example.bankingapi.service;

import com.example.bankingapi.dto.TransactionRequest;
import com.example.bankingapi.dto.TransferRequest;
import com.example.bankingapi.model.Account;
import com.example.bankingapi.model.Transaction;
import com.example.bankingapi.repository.AccountRepository;
import com.example.bankingapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class TransactionService {

    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Account deposit(Long id, TransactionRequest request) {
        Account account = findAccountById(id);
        validateAmountGreaterThanZero(request.getAmount());
        updateBalance(account, request.getAmount(), "DEPOSIT");
        return account;
    }

    @Transactional
    public Account withdraw(Long id, TransactionRequest request) {
        Account account = findAccountById(id);
        validatePin(account, request.getPin());
        validateAmountGreaterThanZero(request.getAmount());
        validateSufficientFunds(account, request.getAmount());
        updateBalance(account, request.getAmount().negate(), "WITHDRAWAL");
        return account;
    }

    @Transactional
    public void transfer(TransferRequest request) {
        Account sourceAccount = findAccountById(request.getSourceAccountId());
        Account destinationAccount = findAccountById(request.getDestinationAccountId());
        validatePin(sourceAccount, request.getPin());
        validateAmountGreaterThanZero(request.getAmount());
        validateSufficientFunds(sourceAccount, request.getAmount());

        performTransfer(sourceAccount, destinationAccount, request.getAmount());
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Account not found"));
    }

    private void updateBalance(Account account, BigDecimal amount, String type) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        saveTransaction(account.getId(), amount, type);
        logger.info(type + " of amount " + amount + " for account ID " + account.getId() + " completed successfully.");
    }

    private void performTransfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        saveTransaction(sourceAccount.getId(), amount.negate(), "TRANSFER");
        saveTransaction(destinationAccount.getId(), amount, "TRANSFER");
        logger.info("Transfer of amount " + amount + " from account ID " + sourceAccount.getId() + " to account ID " + destinationAccount.getId() + " completed successfully.");
    }

    private void saveTransaction(Long accountId, BigDecimal amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    private void validateAmountGreaterThanZero(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private void validatePin(Account account, String pin) {
        if (!account.getPin().equals(pin)) {
            throw new IllegalArgumentException("Invalid PIN");
        }
    }

    private void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}
