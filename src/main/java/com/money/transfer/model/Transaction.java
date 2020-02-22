package com.money.transfer.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "sourceAccountNumber", nullable = false)
    private long sourceAccountNumber;

    @Column(name = "destinationAccountNumber", nullable = false)
    private long destinationAccountNumber;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(long sourceAccountNumber, long destinationAccountNumber, BigDecimal amount) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
    }

    public long getId() {
        return id;
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
        return "Transaction{" +
                "id=" + id +
                ", sourceAccountNumber=" + sourceAccountNumber +
                ", destinationAccountNumber=" + destinationAccountNumber +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id &&
                sourceAccountNumber == that.sourceAccountNumber &&
                destinationAccountNumber == that.destinationAccountNumber &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceAccountNumber, destinationAccountNumber, amount);
    }
}
