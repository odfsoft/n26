package com.n26.controller;

import com.n26.dto.TransactionDTO;
import com.n26.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Folger on 10/29/2016.
 */
@RestController
@RequestMapping(value = "transactionservice", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveTransaction(@PathVariable @NotNull Long transactionId, @RequestBody @Valid TransactionDTO transactionDto) {
        transactionService.save(transactionId, transactionDto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TransactionDTO getTransaction(@PathVariable @NotNull Long transactionId) {
        return transactionService.findTransactionById(transactionId);
    }

    @RequestMapping(value = "/types/{transactionType}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Set<Long> getTransactionsByType(@PathVariable @NotNull String transactionType) {
        return transactionService.findTransactionsByType(transactionType);
    }

    @RequestMapping(value = "/sum/{transactionId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Double calculateSumByTransaction(@PathVariable @NotNull Long transactionId) {
        return transactionService.calculateSumByTransactionId(transactionId);
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
