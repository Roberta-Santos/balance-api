package br.com.robertasantos.balanceapi.unit.service;

import br.com.robertasantos.balanceapi.repository.AccountRepositoryImpl;
import br.com.robertasantos.balanceapi.service.BalanceService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BalanceServiceTest {

    @Test
    void getBalance_returnsBalanceWhenAccountExists() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 10);

        BalanceService service = new BalanceService(repo);
        assertEquals(Optional.of(10), service.getBalance("100"));
    }

    @Test
    void getBalance_returnsEmptyWhenAccountDoesNotExist() {
        BalanceService service = new BalanceService(new AccountRepositoryImpl());
        assertEquals(Optional.empty(), service.getBalance("999"));
    }
}
