package com.money.transfer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO {

    @JsonProperty
    private long accountNumber;

    @NotNull
    @NotEmpty
    @JsonProperty
    private String name;

    @NotNull
    @JsonProperty
    @DecimalMin(value = "0.0")
    private BigDecimal balance;

    public AccountDTO() {
    }

    public AccountDTO(long accountNumber, String name, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public AccountDTO(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
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
        return "AccountDTO{" +
                "accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDTO)) return false;
        AccountDTO that = (AccountDTO) o;
        return getAccountNumber() == that.getAccountNumber() &&
                Objects.equals(getName(), that.getName()) &&
                getBalance().compareTo(that.getBalance()) == 0;
    }

}
