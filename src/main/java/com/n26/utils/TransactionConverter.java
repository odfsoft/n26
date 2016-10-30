package com.n26.utils;

import com.n26.domain.Transaction;
import com.n26.dto.TransactionDTO;
import org.springframework.stereotype.Component;

/**
 * Created by Folger on 10/29/2016.
 */
@Component
public class TransactionConverter {

    public Transaction convert(Long transactionId, TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction(transactionId, transactionDTO.getType(), transactionDTO.getAmount());
        transaction.setParentId(transactionDTO.getParentId());
        return transaction;
    }

    public TransactionDTO convert(Transaction transaction) {
        return new TransactionDTO(transaction.getParentId(), transaction.getType(), transaction.getAmount());
    }

}
