package com.money.transfer.dao;

import com.money.transfer.model.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDAO extends AbstractDAO<Account> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

    public AccountDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Account saveOrUpdate(Account account) {
        LOGGER.info("Saving/Updating Account: " + account.toString());
        return persist(account);
    }

    public Account findByAccountNumber(long accountNumber) {
        LOGGER.info("Fetching account info for account number: " + accountNumber);
        return currentSession().get(Account.class, accountNumber);
    }

}
