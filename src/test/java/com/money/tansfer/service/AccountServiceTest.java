package com.money.tansfer.service;

import com.money.transfer.dao.AccountDao;
import com.money.transfer.dto.AccountDTO;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.mapper.AccountMapper;
import com.money.transfer.model.Account;
import com.money.transfer.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    private AccountService accountService;

    @Mock
    private AccountDao accountDao;

    @Mock
    private AccountMapper accountMapper;

    @Before
    public void setup() {
        accountService = new AccountService(accountDao, accountMapper);
    }

    @Test
    public void accountCreationTest() {
        long testAccountId = 29;
        String testAccountName = "Chirag Jain";
        BigDecimal testAmount = BigDecimal.valueOf(101.01);

        AccountDTO testAccountDTO = new AccountDTO(testAccountId, testAccountName, testAmount);

        Account testAccountToBeCreated = new Account(testAccountName, testAmount);
        Account testAccountCreated = new Account(testAccountName, testAmount);

        when(accountMapper.dtoToEntity(testAccountDTO)).thenReturn(testAccountToBeCreated);
        when(accountDao.saveOrUpdate(testAccountToBeCreated)).thenReturn(testAccountCreated);
        when(accountMapper.entityToDto(testAccountCreated)).thenReturn(testAccountDTO);

        assertEquals(testAccountDTO, accountService.create(testAccountDTO));
    }

    @Test
    public void fetchValidAccountTest() throws BusinessException {
        long testAccountId = 29;
        String testAccountName = "Chirag Jain";
        BigDecimal testAmount = BigDecimal.valueOf(101.01);

        AccountDTO testAccountDTO = new AccountDTO(testAccountId, testAccountName, testAmount);
        Account testAccount = new Account(testAccountName, testAmount);

        when(accountDao.findByAccountNumber(testAccountId)).thenReturn(testAccount);
        when(accountMapper.entityToDto(testAccount)).thenReturn(testAccountDTO);

        assertEquals(testAccountDTO, accountService.get(testAccountId));
    }

    @Test(expected = BusinessException.class)
    public void fetchInvalidAccountTest() throws BusinessException {
        long testAccountId = 39;
        when(accountDao.findByAccountNumber(testAccountId)).thenReturn(null);

        accountService.get(testAccountId);
    }
}
