package br.com.robertasantos.balanceapi.repository;

import java.util.Optional;

public interface AccountRepository {

    Optional<Integer> findBalance(String accountId);

    int deposit(String accountId, int amount);

    /**
     * @return resulting balance if origin exists, empty otherwise
     */
    Optional<Integer> withdraw(String accountId, int amount);

    /**
     * @return resulting balances if origin exists, empty otherwise
     */
    Optional<TransferResult> transfer(String originAccountId, String destinationAccountId, int amount);

    void reset();

    record TransferResult(int originBalance, int destinationBalance) {}
}
