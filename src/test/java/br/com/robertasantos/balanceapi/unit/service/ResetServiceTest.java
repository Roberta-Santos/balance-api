package br.com.robertasantos.balanceapi.unit.service;

import br.com.robertasantos.balanceapi.repository.AccountRepository;
import br.com.robertasantos.balanceapi.repository.AccountRepositoryImpl;
import br.com.robertasantos.balanceapi.service.ResetService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResetServiceTest {

    static class TestRepo implements AccountRepository {
        boolean resetCalled = false;

        @Override
        public java.util.Optional<Integer> findBalance(String accountId) {
            return java.util.Optional.empty();
        }

        @Override
        public int deposit(String accountId, int amount) {
            return 0;
        }

        @Override
        public java.util.Optional<Integer> withdraw(String accountId, int amount) {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.Optional<TransferResult> transfer(String originAccountId, String destinationAccountId, int amount) {
            return java.util.Optional.empty();
        }

        @Override
        public void reset() {
            resetCalled = true;
        }
    }

    @Test
    void reset_invokesRepositoryReset() {
        TestRepo repo = new TestRepo();
        ResetService s = new ResetService(repo);
        assertFalse(repo.resetCalled);
        s.reset();
        assertTrue(repo.resetCalled);
    }

    @Test
    void reset_clearsRepositoryState() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 10);

        ResetService service = new ResetService(repo);
        service.reset();

        assertTrue(repo.findBalance("100").isEmpty());
    }
}
