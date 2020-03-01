package com.money.tansfer.service;

import com.money.transfer.dao.AccountDAO;
import com.money.transfer.dao.TransactionDAO;
import com.money.transfer.dto.TransactionDTO;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.exception.ErrorMessage;
import com.money.transfer.mapper.TransactionMapper;
import com.money.transfer.model.Account;
import com.money.transfer.model.Transaction;
import com.money.transfer.service.TransactionService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private AccountDAO accountDao;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionDAO transactionDAO;

    private TransactionService transactionService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        transactionService = new TransactionService(accountDao, transactionDAO, transactionMapper);
    }

    @Test
    public void transactWithInvalidSourceAccountTest() throws BusinessException {
        TransactionDTO transactionDTO = new TransactionDTO(29L, 39L, BigDecimal.valueOf(1000));

        when(accountDao.findByAccountNumber(transactionDTO.getSourceAccountNumber())).thenReturn(null);

        expectedException.expect(BusinessException.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ErrorMessage.SOURCE_ACCOUNT_NOT_EXISTS.message()));

        transactionService.processTransaction(transactionDTO);
    }

    @Test
    public void transactWithInvalidDestinationAccountTest() throws BusinessException {
        TransactionDTO transactionDTO = new TransactionDTO(29L, 39L, BigDecimal.valueOf(1000));

        when(accountDao.findByAccountNumber(transactionDTO.getSourceAccountNumber())).thenReturn(new Account());
        when(accountDao.findByAccountNumber(transactionDTO.getDestinationAccountNumber())).thenReturn(null);

        expectedException.expect(BusinessException.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ErrorMessage.DESTINATION_ACCOUNT_NOT_EXISTS.message()));

        transactionService.processTransaction(transactionDTO);
    }

    @Test
    public void transactWithInsufficientBalanceTest() throws BusinessException {
        TransactionDTO transactionDTO = new TransactionDTO(29L, 39L, BigDecimal.valueOf(1000));

        Account sourceAccount = new Account("Source Account Name", BigDecimal.valueOf(50));
        Account destinationAccount = new Account("Destination Account Name", BigDecimal.valueOf(100));

        when(accountDao.findByAccountNumber(transactionDTO.getSourceAccountNumber())).thenReturn(sourceAccount);
        when(accountDao.findByAccountNumber(transactionDTO.getDestinationAccountNumber())).thenReturn(destinationAccount);

        expectedException.expect(BusinessException.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ErrorMessage.INSUFFICIENT_BALANCE.message()));

        transactionService.processTransaction(transactionDTO);
    }

    @Test
    public void validTransactionTest() throws BusinessException {
        TransactionDTO transactionDTO = new TransactionDTO(29L, 39L, BigDecimal.valueOf(40));
        Account sourceAccount = new Account("Source Account Name", BigDecimal.valueOf(250));
        Account destinationAccount = new Account("Destination Account Name", BigDecimal.valueOf(150));

        Account updatedSourceAccount = new Account("Source Account Name", BigDecimal.valueOf(210));
        Account updatedDestinationAccount = new Account("Destination Account Name", BigDecimal.valueOf(190));
        Transaction testTransaction = new Transaction(sourceAccount.getAccountNumber(),
                destinationAccount.getAccountNumber(),
                transactionDTO.getAmount());

        when(accountDao.findByAccountNumber(transactionDTO.getSourceAccountNumber())).thenReturn(sourceAccount);
        when(accountDao.findByAccountNumber(transactionDTO.getDestinationAccountNumber())).thenReturn(destinationAccount);
        when(transactionDAO.saveOrUpdate(any(Transaction.class))).thenReturn(testTransaction);

        transactionService.processTransaction(transactionDTO);

        ArgumentCaptor<Account> accountEntityArgCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<Transaction> transactionEntityArgCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(accountDao, times(2)).saveOrUpdate(accountEntityArgCaptor.capture());
        verify(transactionDAO, times(1)).saveOrUpdate(transactionEntityArgCaptor.capture());

        assertEquals(updatedSourceAccount, accountEntityArgCaptor.getAllValues().get(0));
        assertEquals(updatedDestinationAccount, accountEntityArgCaptor.getAllValues().get(1));

        assertEquals(testTransaction, transactionEntityArgCaptor.getAllValues().get(0));
    }
}
