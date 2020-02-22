package com.money.transfer.exception;

public enum ErrorMessage {
    ACCOUNT_NOT_FOUND("No account found for given account number"),
    SOURCE_ACCOUNT_NOT_EXISTS("Source account not found for given account number"),
    DESTINATION_ACCOUNT_NOT_EXISTS("Destination account not found for given account number"),
    SOURCE_ACCOUNT_STATE_CHANGED("Your account was updated by another transaction"),
    DESTINATION_ACCOUNT_STATE_CHANGED("Your account was updated by another transaction"),
    INSUFFICIENT_BALANCE("Insufficient balance to perform this transaction");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
