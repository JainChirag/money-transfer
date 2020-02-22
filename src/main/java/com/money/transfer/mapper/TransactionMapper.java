package com.money.transfer.mapper;

import com.money.transfer.dto.TransactionDTO;
import com.money.transfer.model.Transaction;

public class TransactionMapper {

    public TransactionDTO entityToDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionDTO(transaction.getSourceAccountNumber(), transaction.getDestinationAccountNumber(), transaction.getAmount());
    }

}
