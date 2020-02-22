package com.money.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

public class TransactionDTO {
    @JsonProperty
    @NotNull
    @Min(value = 0L, message = "Source account number must be a positive number")
    private long sourceAccountNumber;


    @JsonProperty
    @NotNull
    @Min(value = 0L, message = "Destination account number must be a positive number")
    private long destinationAccountNumber;

    @JsonProperty
    @NotNull
    @DecimalMin(value = "0.0", message = "Transaction amount must be a positive value")
    private BigDecimal amount;

    public TransactionDTO() {
    }

    public TransactionDTO(long sourceAccountNumber, long destinationAccountNumber, BigDecimal amount) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
    }

    public long getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(long sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public long getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(long destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "sourceAccountNumber=" + sourceAccountNumber +
                ", destinationAccountNumber=" + destinationAccountNumber +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDTO that = (TransactionDTO) o;
        return sourceAccountNumber == that.sourceAccountNumber &&
                destinationAccountNumber == that.destinationAccountNumber &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceAccountNumber, destinationAccountNumber, amount);
    }
}
