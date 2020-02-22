package com.money.transfer.exception;

public enum ErrorMessage {
    ACCOUNT_NOT_FOUND("No account found for this account number");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
