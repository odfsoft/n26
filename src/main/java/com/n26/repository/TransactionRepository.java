package com.n26.repository;

import com.n26.domain.Transaction;
import com.n26.utils.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Folger on 10/29/2016.
 */
@Repository
public class TransactionRepository extends AbstractRepository<Transaction> {

    @Autowired
    TransactionValidator transactionValidator;

    public TransactionRepository() {
    }

    @PostConstruct
    public void initValidator() {
        setModelValidator(transactionValidator);
    }

    public Set<Transaction> findByType(String type) {
        return models.entrySet().stream()
                .filter(transactionEntry -> Objects.equals(transactionEntry.getValue().getType(), type))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public void updateParentTransaction(Transaction transaction) {
        if(transaction.getParentId() != null) {
            models.get(transaction.getParentId()).getChildrenTransactions().add(transaction.getId());
        }
    }

    public Set<Transaction> findByIds(Set<Long> childrenTransactions) {
        return childrenTransactions
                .stream()
                .map(transactionId -> findById(transactionId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public void cleanAll() {
        models.clear();
    }
}
