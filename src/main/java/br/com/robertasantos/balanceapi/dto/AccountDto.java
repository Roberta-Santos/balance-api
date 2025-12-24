package br.com.robertasantos.balanceapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDto {

    private String id;
    private int balance;

    public AccountDto() {}

    public AccountDto(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("balance")
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
