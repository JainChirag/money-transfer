package com.money.transfer.service;

import com.money.transfer.dao.AccountDAO;
import com.money.transfer.dao.TransactionDAO;
import com.money.transfer.dto.TransactionDTO;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.exception.ErrorMessage;
import com.money.transfer.exception.ErrorResponse;
import com.money.transfer.mapper.TransactionMapper;
import com.money.transfer.model.Account;
import com.money.transfer.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private AccountDAO accountDao;

    private TransactionDAO transactionDAO;

    private TransactionMapper transactionMapper;

    public TransactionService(AccountDAO accountDAO, TransactionDAO transactionDAO, TransactionMapper transactionMapper) {
        this.accountDao = accountDAO;
        this.transactionDAO = transactionDAO;
        this.transactionMapper = transactionMapper;
    }

    public TransactionDTO processTransaction(TransactionDTO transactionDTO) throws BusinessException {
        BigDecimal transferAmount = transactionDTO.getAmount();

        Account sourceAccount = accountDao.findByAccountNumber(transactionDTO.getSourceAccountNumber());
        Account destinationAccount = accountDao.findByAccountNumber(transactionDTO.getDestinationAccountNumber());

        validateTransferRequest(sourceAccount, destinationAccount, transferAmount);

        Transaction transaction = performTransaction(sourceAccount, destinationAccount, transactionDTO);

        LOGGER.info("Transaction Completed Successfully. Transaction Details: " + transaction.toString());
        return transactionMapper.entityToDto(transaction);
    }

    private void validateTransferRequest(Account sourceAccount, Account destinationAccount, BigDecimal transferAmount) throws BusinessException {
        if (sourceAccount == null) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.NOT_FOUND.getStatusCode(), ErrorMessage.SOURCE_ACCOUNT_NOT_EXISTS.message());
            throw new BusinessException(errorResponse);
        }
        if (destinationAccount == null) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.NOT_FOUND.getStatusCode(), ErrorMessage.DESTINATION_ACCOUNT_NOT_EXISTS.message());
            throw new BusinessException(errorResponse);
        }
        if (!userHasSufficientBalance(sourceAccount.getBalance(), transferAmount)) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), ErrorMessage.INSUFFICIENT_BALANCE.message());
            throw new BusinessException(errorResponse);
        }
        LOGGER.info("Valid Transfer Request");
    }

    private Transaction performTransaction(Account sourceAccount, Account destinationAccount, TransactionDTO transactionDTO) throws BusinessException {
        BigDecimal transferAmount = transactionDTO.getAmount();

        debitAccount(sourceAccount, transferAmount);
        creditAccount(destinationAccount, transferAmount);

        Transaction transaction = new Transaction(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), transferAmount);
        return transactionDAO.saveOrUpdate(transaction);
    }

    private boolean userHasSufficientBalance(BigDecimal userBalance, BigDecimal amountToTransfer) {
        return (userBalance.subtract(amountToTransfer).compareTo(BigDecimal.ZERO) >= 0);
    }

    private void debitAccount(Account account, BigDecimal debitAmount) throws BusinessException {
        BigDecimal newBalance = account.getBalance().subtract(debitAmount);
        account.setBalance(newBalance);
        accountDao.saveOrUpdate(account);
    }

    private void creditAccount(Account account, BigDecimal creditAmount) throws BusinessException {
        BigDecimal newBalance = account.getBalance().add(creditAmount);
        account.setBalance(newBalance);
        accountDao.saveOrUpdate(account);
    }
}