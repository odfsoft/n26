package com.n26.service;

import com.n26.domain.Transaction;
import com.n26.dto.TransactionDTO;
import com.n26.repository.TransactionRepository;
import com.n26.exception.ResourceNotFoundException;
import com.n26.utils.TransactionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Folger on 10/29/2016.
 */
@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionConverter transactionConverter;

    public TransactionDTO findTransactionById(Long id) {
        return transactionConverter.convert(
                transactionRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Error, transaction id not found.")));
    }

    public void save(Long transactionId, TransactionDTO transactionDTO) {
        Transaction transaction = transactionConverter.convert(transactionId, transactionDTO);
        transactionRepository.save(transaction);
        transactionRepository.updateParentTransaction(transaction);
    }

    public Set<Long> findTransactionsByType(String type) {
        return transactionRepository.findByType(type)
                .stream()
                .map(Transaction::getId)
                .collect(Collectors.toSet());
    }

    /*
        Since it was never mention in the test, I assume that you can build this tree like transaction structure.
        in any case should cover the basic and complex case.
     */
    public Double calculateSumByTransactionId(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new ResourceNotFoundException("Error, transaction id not found."));
        return calculateSumForChildren(transaction);
    }

    /*
        the sum of the child elements can be performed in parallel since the are not codependent to have
        the global total of the sum.
     */
    private Double calculateSumForChildren(Transaction transaction) {
        Set<Transaction> childrenTransactions = transactionRepository.findByIds(transaction.getChildrenTransactions());
        if(childrenTransactions.isEmpty()) {
            return transaction.getAmount();
        } else {
            return transaction.getAmount() +
                    childrenTransactions
                    .parallelStream()
                    .mapToDouble(childTransaction -> calculateSumForChildren(childTransaction)).sum();
        }
    }

    /*
        This method was only implement to help the testing process.
     */
    public void cleanTransactions() {
        transactionRepository.cleanAll();
    }
}
