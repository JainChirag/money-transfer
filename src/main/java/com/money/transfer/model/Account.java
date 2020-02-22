package com.money.transfer.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@SelectBeforeUpdate
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Version
    private long version;

    public Account() {
    }

    public Account(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", version=" + version +
                '}';
    }
}
