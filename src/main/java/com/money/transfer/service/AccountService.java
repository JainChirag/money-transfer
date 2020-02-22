package com.money.transfer.service;

import com.money.transfer.dao.AccountDAO;
import com.money.transfer.dto.AccountDTO;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.exception.ErrorMessage;
import com.money.transfer.exception.ErrorResponse;
import com.money.transfer.mapper.AccountMapper;
import com.money.transfer.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.Objects;

public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final AccountDAO accountDao;
    private final AccountMapper accountMapper;

    public AccountService(AccountDAO accountDao, AccountMapper accountMapper) {
        this.accountDao = accountDao;
        this.accountMapper = accountMapper;
    }

    public AccountDTO create(AccountDTO newAccount) {
        LOGGER.info("Creating a new Account with details: {}", newAccount.toString());
        Account account = accountMapper.dtoToEntity(newAccount);
        Account createdAccount = accountDao.saveOrUpdate(account);
        return accountMapper.entityToDto(createdAccount);
    }

    public AccountDTO get(long accountId) throws BusinessException {
        LOGGER.info("Fetching Account Details for account number: {}", accountId);
        Account account = accountDao.findByAccountNumber(accountId);
        if (Objects.isNull(account)) {
            LOGGER.info("Account not found for account number: {}", accountId);
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.NOT_FOUND.getStatusCode(), ErrorMessage.ACCOUNT_NOT_FOUND.message());
            throw new BusinessException(errorResponse);
        }
        return accountMapper.entityToDto(account);
    }
}
