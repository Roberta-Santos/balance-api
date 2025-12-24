package br.com.robertasantos.balanceapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepositResponseDto {

    private AccountDto destination;

    public DepositResponseDto() {}

    public DepositResponseDto(AccountDto destination) {
        this.destination = destination;
    }

    @JsonProperty("destination")
    public AccountDto getDestination() {
        return destination;
    }

    public void setDestination(AccountDto destination) {
        this.destination = destination;
    }
}
