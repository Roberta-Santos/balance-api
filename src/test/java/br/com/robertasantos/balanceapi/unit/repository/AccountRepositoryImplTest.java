package br.com.robertasantos.balanceapi.unit.repository;

import br.com.robertasantos.balanceapi.repository.AccountRepository.TransferResult;
import br.com.robertasantos.balanceapi.repository.AccountRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryImplTest {

    @Test
    void deposit_createsOrUpdatesBalance() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();

        assertTrue(repo.findBalance("100").isEmpty());

        int b1 = repo.deposit("100", 10);
        assertEquals(10, b1);
        assertEquals(10, repo.findBalance("100").orElseThrow());

        int b2 = repo.deposit("100", 5);
        assertEquals(15, b2);
        assertEquals(15, repo.findBalance("100").orElseThrow());
    }

    @Test
    void withdraw_returnsEmptyWhenAccountDoesNotExist() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        assertTrue(repo.withdraw("nope", 10).isEmpty());
    }

    @Test
    void withdraw_updatesBalanceWhenAccountExists() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 20);

        int b = repo.withdraw("100", 7).orElseThrow();
        assertEquals(13, b);
        assertEquals(13, repo.findBalance("100").orElseThrow());
    }

    @Test
    void transfer_returnsEmptyWhenOriginDoesNotExist() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        assertTrue(repo.transfer("origin", "dest", 10).isEmpty());
    }

    @Test
    void transfer_updatesBothAccountsAtomically() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 20);

        TransferResult r1 = repo.transfer("100", "300", 15).orElseThrow();
        assertEquals(5, r1.originBalance());
        assertEquals(15, r1.destinationBalance());

        // destination already exists: merge branch
        TransferResult r2 = repo.transfer("100", "300", 5).orElseThrow();
        assertEquals(0, r2.originBalance());
        assertEquals(20, r2.destinationBalance());
    }

    @Test
    void reset_clearsAllBalances() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 10);
        repo.deposit("200", 20);

        repo.reset();

        assertTrue(repo.findBalance("100").isEmpty());
        assertTrue(repo.findBalance("200").isEmpty());
    }
}
