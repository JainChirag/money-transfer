package com.money.tansfer.mapper;

import com.money.transfer.dto.AccountDTO;
import com.money.transfer.mapper.AccountMapper;
import com.money.transfer.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AccountMapperTest {

    private AccountMapper accountMapper;

    @Before
    public void setup() {
        accountMapper = new AccountMapper();
    }

    @Test
    public void entityToDtoTest() {
        Account testAccountEntity = new Account("Chirag Jain", BigDecimal.valueOf(100.00));
        AccountDTO mappedAccountDTO = accountMapper.entityToDto(testAccountEntity);

        assertEquals(testAccountEntity.getAccountNumber(), mappedAccountDTO.getAccountNumber());
        assertEquals(testAccountEntity.getName(), mappedAccountDTO.getName());
        assertEquals(testAccountEntity.getBalance(), mappedAccountDTO.getBalance());
    }

    @Test
    public void entityToDtoNullTest() {
        AccountDTO mappedAccountDTO = accountMapper.entityToDto(null);

        assertNull(mappedAccountDTO);
    }

    @Test
    public void dtoToEntityTest() {
        AccountDTO testAccountDto = new AccountDTO("Chirag Jain", BigDecimal.valueOf(100.00));
        Account mappedAccountEntity = accountMapper.dtoToEntity(testAccountDto);

        assertEquals(testAccountDto.getName(), mappedAccountEntity.getName());
        assertEquals(testAccountDto.getBalance(), mappedAccountEntity.getBalance());
    }

    @Test
    public void dtoToEntityNullTest() {
        Account mappedAccountEntity = accountMapper.dtoToEntity(null);

        assertNull(mappedAccountEntity);
    }
}
