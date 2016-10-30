package com.n26.service;

import com.n26.Application;
import com.n26.dto.TransactionDTO;
import com.n26.exception.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TransactionServiceTest{

    @Autowired
    private TransactionService transactionService;

    @Before
    public void cleanTransactions() {
        transactionService.cleanTransactions();
    }

    @Test
    public void testSaveTransaction() throws Exception {
        transactionService.save(10L, getTransactionDTO());
        TransactionDTO transaction = transactionService.findTransactionById(10L);
        assertEquals(5000D, transaction.getAmount().doubleValue());
    }


    @Test(expected = ResourceNotFoundException.class)
    public void testGetTransactionByIdWithWrongId() {
        transactionService.findTransactionById(15L);
    }

    @Test
    public void testGetTransactionsByType() throws Exception {
        transactionService.save(1L, getTransactionDTO());
        transactionService.save(2L, getTransactionDTO());
        transactionService.save(3L, getTransactionDTO());
        Set<Long> transactions = transactionService.findTransactionsByType("cars");
        Assertions.assertThat(transactions).isNotNull();
        Assertions.assertThat(transactions).isNotEmpty();
        Assertions.assertThat(transactions.size()).isEqualTo(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithWrongParentId() throws Exception {
        TransactionDTO transactionDTO = getTransactionDTO();
        transactionDTO.setParentId(100L);
        transactionService.save(1L, transactionDTO);
    }

    @Test
    public void testSaveWithParentIdIsOK() throws Exception {
        TransactionDTO parentTransactionDTO = getTransactionDTO();
        transactionService.save(10L, parentTransactionDTO);

        TransactionDTO transactionDTO = getTransactionDTO();
        transactionDTO.setParentId(10L);
        transactionService.save(11L, transactionDTO);

        Assertions.assertThat(transactionService.findTransactionById(11L)).isNotNull();
    }

    @Test
    public void testCalculateSumWithParentTransactionId() throws Exception {
        createTestTransactions();

        Double sum = transactionService.calculateSumByTransactionId(11L);
        Assertions.assertThat(sum).isNotNull();

        Assertions.assertThat(sum).isNotNull();
        Assertions.assertThat(sum.doubleValue()).isEqualTo(20000);
    }

    @Test
    public void testCalculateSumWithParentTransactionIdForRootLevelWithMultipleChildrenLevels() throws Exception {
        createTestTransactions();

        Double sum = transactionService.calculateSumByTransactionId(10L);
        Assertions.assertThat(sum).isNotNull();

        Assertions.assertThat(sum).isNotNull();
        Assertions.assertThat(sum.doubleValue()).isEqualTo(35000);
    }


    @Test
    public void testCalculateSumWithParentTransactionIdForLeafLevelWithMultipleChildrenLevels() throws Exception {
        createTestTransactions();

        Double sum = transactionService.calculateSumByTransactionId(16L);
        Assertions.assertThat(sum).isNotNull();

        Assertions.assertThat(sum).isNotNull();
        Assertions.assertThat(sum.doubleValue()).isEqualTo(5000);
    }

    private void createTestTransactions() {
        TransactionDTO parentTransactionDTO = getTransactionDTO();
        transactionService.save(10L, parentTransactionDTO);

        TransactionDTO transactionDTO = getTransactionDTO();
        transactionDTO.setParentId(10L);
        transactionService.save(11L, transactionDTO);
        transactionService.save(12L, transactionDTO);
        transactionService.save(13L, transactionDTO);

        TransactionDTO transactionDTOAnother = getTransactionDTO();
        transactionDTOAnother.setParentId(11L);
        transactionService.save(14L, transactionDTOAnother);
        transactionService.save(15L, transactionDTOAnother);
        transactionService.save(16L, transactionDTOAnother);
    }


    private TransactionDTO getTransactionDTO() {
        TransactionDTO transactionDTO = new TransactionDTO("cars", 5000D);
        return transactionDTO;
    }
}