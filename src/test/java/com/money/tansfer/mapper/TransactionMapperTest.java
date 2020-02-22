package com.money.tansfer.mapper;

import com.money.transfer.dto.TransactionDTO;
import com.money.transfer.mapper.TransactionMapper;
import com.money.transfer.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @Before
    public void setup() {
        transactionMapper = new TransactionMapper();
    }

    @Test
    public void entityToDtoTest() {
        Transaction testTransaction = new Transaction(29L, 39L, BigDecimal.valueOf(101.01));
        TransactionDTO mappedTransactionDTO = transactionMapper.entityToDto(testTransaction);

        assertEquals(testTransaction.getSourceAccountNumber(), mappedTransactionDTO.getSourceAccountNumber());
        assertEquals(testTransaction.getDestinationAccountNumber(), mappedTransactionDTO.getDestinationAccountNumber());
        assertEquals(testTransaction.getAmount(), mappedTransactionDTO.getAmount());
    }

    @Test
    public void entityToDtoNullTest() {
        TransactionDTO mappedTransactionDTO = transactionMapper.entityToDto(null);

        assertNull(mappedTransactionDTO);
    }
}
