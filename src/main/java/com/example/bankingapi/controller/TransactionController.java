package com.example.bankingapi.controller;

import com.example.bankingapi.dto.TransactionRequest;
import com.example.bankingapi.dto.TransferRequest;
import com.example.bankingapi.model.Account;
import com.example.bankingapi.model.Transaction;
import com.example.bankingapi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit/{id}")
    public ResponseEntity<Account> deposit(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        try {
            return ResponseEntity.ok(transactionService.deposit(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/withdraw/{id}")
    public ResponseEntity<Account> withdraw(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        try {
            return ResponseEntity.ok(transactionService.withdraw(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        try {
            transactionService.transfer(request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactions(id));
    }
}
