package br.com.robertasantos.balanceapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferResponseDto {

    private AccountDto origin;
    private AccountDto destination;

    public TransferResponseDto() {}

    public TransferResponseDto(AccountDto origin, AccountDto destination) {
        this.origin = origin;
        this.destination = destination;
    }

    @JsonProperty("origin")
    public AccountDto getOrigin() {
        return origin;
    }

    public void setOrigin(AccountDto origin) {
        this.origin = origin;
    }

    @JsonProperty("destination")
    public AccountDto getDestination() {
        return destination;
    }

    public void setDestination(AccountDto destination) {
        this.destination = destination;
    }
}
