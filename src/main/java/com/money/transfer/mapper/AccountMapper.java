package com.money.transfer.mapper;

import com.money.transfer.dto.AccountDTO;
import com.money.transfer.model.Account;

import java.util.Objects;

public class AccountMapper {

    public AccountDTO entityToDto(Account account) {
        if (Objects.isNull(account)) {
            return null;
        }
        return new AccountDTO(account.getAccountNumber(), account.getName(), account.getBalance());
    }

    public Account dtoToEntity(AccountDTO accountDTO) {
        if (Objects.isNull(accountDTO)) {
            return null;
        }
        return new Account(accountDTO.getName(), accountDTO.getBalance());
    }
}
