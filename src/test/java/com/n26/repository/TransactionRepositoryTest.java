package com.n26.repository;


import com.n26.Application;
import com.n26.domain.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TransactionRepositoryTest {

    public static final String CARS_TYPE = "cars";
    @Autowired
    private  TransactionRepository transactionRepository;

    @Before
    public void cleanTransactions() {
        transactionRepository.cleanAll();
    }

    @Test
    public void testSaveWithCorrectData() throws Exception {
        Transaction transaction = new Transaction(1L, CARS_TYPE, 3000D);
        transactionRepository.save(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithIdAlreadyInUseShouldNotWork() throws Exception {
        createTestTransactions();
        Transaction transaction = new Transaction(10L, CARS_TYPE, 6000D);
        transactionRepository.save(transaction);
    }

        @Test(expected = IllegalArgumentException.class)
    public void testSaveWithNotValidParentId() throws Exception {
        Transaction transaction = new Transaction(1L, CARS_TYPE, 3000D);
        transaction.setParentId(600L);
        transactionRepository.save(transaction);
    }

    @Test
    public void testFindByType() throws Exception {
        createTestTransactions();

        Set<Transaction> transactionIdsByType = transactionRepository.findByType(CARS_TYPE);

        Assertions.assertThat(transactionIdsByType).isNotNull();
        Assertions.assertThat(transactionIdsByType).isNotEmpty();
        Assertions.assertThat(transactionIdsByType.size()).isEqualTo(3);
    }

    @Test
    public void testUpdateParentTransaction() throws Exception {
        createTestTransactions();
        Optional<Transaction> parentTransaction = transactionRepository.findById(10L);
        Optional<Transaction> transaction = transactionRepository.findById(11L);

        transactionRepository.updateParentTransaction(transaction.get());

        Assertions.assertThat(parentTransaction.get().getChildrenTransactions()).isNotEmpty();
        Assertions.assertThat(parentTransaction.get().getChildrenTransactions().size()).isEqualTo(1);
    }

    @Test
    public void testFindChildrenTransactions() throws Exception {
        createTestTransactions();
        Optional<Transaction> parentTransaction = transactionRepository.findById(10L);
        Optional<Transaction> transaction = transactionRepository.findById(11L);
        Optional<Transaction> anotherTransaction = transactionRepository.findById(12L);

        transactionRepository.updateParentTransaction(transaction.get());
        transactionRepository.updateParentTransaction(anotherTransaction.get());

        Set<Transaction> transactions = transactionRepository.findByIds(parentTransaction.get().getChildrenTransactions());

        Assertions.assertThat(transactions).isNotEmpty();
        Assertions.assertThat(transactions.size()).isEqualTo(2);
    }

    @Test
    public void testFindByIds() throws Exception {
        createTestTransactions();

        Optional<Transaction> transaction = transactionRepository.findById(11L);

        Assertions.assertThat(transaction).isPresent();
        Assertions.assertThat(transaction.get().getAmount()).isEqualTo(5000D);
    }

    private void createTestTransactions() {
        Transaction transaction = new Transaction(10L, CARS_TYPE, 5000D);
        Transaction anotherTransaction = new Transaction(11L, CARS_TYPE, 5000D);
        Transaction yetAnotherTransaction = new Transaction(12L, CARS_TYPE, 5000D);
        anotherTransaction.setParentId(10L);
        yetAnotherTransaction.setParentId(10L);

        transactionRepository.save(transaction);
        transactionRepository.save(anotherTransaction);
        transactionRepository.save(yetAnotherTransaction);
    }
}