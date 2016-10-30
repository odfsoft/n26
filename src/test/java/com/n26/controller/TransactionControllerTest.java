package com.n26.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.dto.TransactionDTO;
import com.n26.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionControllerTest {

    public static final String TRANSACTION_APP_ENDPOINT = "/transactionservice";
    public static final String TRANSACTION_ENDPOINT = "/transaction";
    public static final String TYPES_ENDPOINT = "/types";
    public static final String SUM_ENDPOINT = "/sum";

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(transactionController)
                .build();
    }

    @Test
    public void testSaveTransactionReturnsResponseCodeOK() throws Exception {
        Mockito.doNothing()
                .when(transactionService).save(Mockito.anyLong(), Mockito.any(TransactionDTO.class));

        TransactionDTO transactionDTO = getTransactionDTO();
        String transactionDTOJSON = objectMapper.writeValueAsString(transactionDTO);

        mvc.perform(MockMvcRequestBuilders.put(TRANSACTION_APP_ENDPOINT + TRANSACTION_ENDPOINT + "/" + "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(transactionDTOJSON))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())    ;
    }

    @Test
    public void testSaveTransactionReturnsBadRequest() throws Exception {
        Mockito.doNothing()
                .when(transactionService).save(Mockito.anyLong(), Mockito.any(TransactionDTO.class));

        mvc.perform(MockMvcRequestBuilders.put(TRANSACTION_APP_ENDPOINT + TRANSACTION_ENDPOINT + "/" + "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest());
    }

    @Test
    public void testGetTransactionById() throws Exception {
        TransactionDTO transactionDTO = getTransactionDTO();

        Mockito.doReturn(transactionDTO)
                .when(transactionService).findTransactionById(Mockito.anyLong());

        String transactionDTOJSON = objectMapper.writeValueAsString(transactionDTO);

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get(TRANSACTION_APP_ENDPOINT + TRANSACTION_ENDPOINT + "/" + "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(transactionDTOJSON))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("type"));
    }

    private TransactionDTO getTransactionDTO() {
        TransactionDTO transactionDTO = new TransactionDTO("cars", 5000D);
        return transactionDTO;
    }

    @Test
    public void testGetTransactionsByType() throws Exception {
        Set<Long> transactionIds = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        Mockito.doReturn(transactionIds)
                .when(transactionService).findTransactionsByType(Mockito.anyString());

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get(TRANSACTION_APP_ENDPOINT + TYPES_ENDPOINT + "/" + "cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status()
                                .isOk())
                        .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.equals(objectMapper.writeValueAsString(transactionIds)));

    }

    @Test
    public void testCalculateSumByTransaction() throws Exception {
        Double sum = 1000D;
        Mockito.doReturn(sum)
                .when(transactionService).calculateSumByTransactionId(Mockito.anyLong());

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get(TRANSACTION_APP_ENDPOINT + SUM_ENDPOINT + "/" + "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status()
                                .isOk())
                        .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.equals(objectMapper.writeValueAsString(sum)));
    }
}