package com.n26.utils;

import com.n26.domain.Transaction;
import com.n26.validator.ModelValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Folger on 10/29/2016.
 */
@Component
public class TransactionValidator implements ModelValidator<Transaction> {

    public void validate(Map<Long, Transaction> transactions, Transaction transaction) {
        validateTransactionId(transactions, transaction.getId());
        validateParentId(transactions, transaction.getParentId());
    }

    private void validateTransactionId(Map<Long, Transaction> transactions, Long id) {
        if(transactions
                .entrySet()
                .stream()
                .anyMatch(transactionEntry -> transactionEntry.getValue().getId().equals(id))) {
            throw new IllegalArgumentException("Error, transactionId already in use.");
        }
    }

    private void validateParentId(Map<Long, Transaction> transactions, Long parentId) {
        if(parentId != null && transactions.entrySet().stream().noneMatch(transactionEntry -> Objects.equals(transactionEntry.getValue().getId(), parentId))) {
            throw new IllegalArgumentException("Error, the parent transaction id does not exists.");
        }
    }

}
