package com.money.tansfer.service;

import com.money.transfer.dao.AccountDAO;
import com.money.transfer.dto.AccountDTO;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.exception.ErrorMessage;
import com.money.transfer.mapper.AccountMapper;
import com.money.transfer.model.Account;
import com.money.transfer.service.AccountService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
    private AccountDAO accountDao;

    @Mock
    private AccountMapper accountMapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        accountService = new AccountService(accountDao, accountMapper);
    }

    @Test
    public void accountCreationTest() {
        long testAccountId = 29L;
        String testAccountName = "Account Name";
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
        long testAccountId = 29L;
        String testAccountName = "Account Name";
        BigDecimal testAmount = BigDecimal.valueOf(101.01);

        AccountDTO testAccountDTO = new AccountDTO(testAccountId, testAccountName, testAmount);
        Account testAccount = new Account(testAccountName, testAmount);

        when(accountDao.findByAccountNumber(testAccountId)).thenReturn(testAccount);
        when(accountMapper.entityToDto(testAccount)).thenReturn(testAccountDTO);

        assertEquals(testAccountDTO, accountService.get(testAccountId));
    }

    @Test
    public void fetchInvalidAccountTest() throws BusinessException {
        long testAccountId = 39L;
        when(accountDao.findByAccountNumber(testAccountId)).thenReturn(null);

        expectedException.expect(BusinessException.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ErrorMessage.ACCOUNT_NOT_FOUND.message()));

        accountService.get(testAccountId);
    }
}
