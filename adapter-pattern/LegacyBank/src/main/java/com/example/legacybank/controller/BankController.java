package com.example.legacybank.controller;

import com.example.legacybank.dto.request.TransactionRequest;
import com.example.legacybank.dto.response.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/transactions")
public class BankController {

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        String id = "TX-" + System.nanoTime();
        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionResponse(id));
    }
}
