package com.money.transfer.dao;

import com.money.transfer.model.Transaction;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionDAO extends AbstractDAO<Transaction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDAO.class);

    public TransactionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Transaction saveOrUpdate(Transaction transaction) {
        LOGGER.debug("Saving/Updating Transaction: " + transaction.toString());
        return persist(transaction);
    }

    public Transaction findByTransactionId(long transactionId) {
        LOGGER.debug("Fetching Transaction for transaction id: " + transactionId);
        return currentSession().get(Transaction.class, transactionId);
    }

}
