package br.com.robertasantos.balanceapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawResponseDto {

    private AccountDto origin;

    public WithdrawResponseDto() {}

    public WithdrawResponseDto(AccountDto origin) {
        this.origin = origin;
    }

    @JsonProperty("origin")
    public AccountDto getOrigin() {
        return origin;
    }

    public void setOrigin(AccountDto origin) {
        this.origin = origin;
    }
}
