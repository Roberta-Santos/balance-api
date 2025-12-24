package br.com.robertasantos.balanceapi.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final ConcurrentHashMap<String, Integer> balances = new ConcurrentHashMap<>();
    private final Object transferLock = new Object();

    @Override
    public Optional<Integer> findBalance(String accountId) {
        return Optional.ofNullable(balances.get(accountId));
    }

    @Override
    public int deposit(String accountId, int amount) {
        return balances.merge(accountId, amount, Integer::sum);
    }

    @Override
    public Optional<Integer> withdraw(String accountId, int amount) {
        Integer current = balances.get(accountId);
        if (current == null) {
            return Optional.empty();
        }
        int next = current - amount;
        balances.put(accountId, next);
        return Optional.of(next);
    }

    @Override
    public Optional<TransferResult> transfer(String originAccountId, String destinationAccountId, int amount) {
        synchronized (transferLock) {
            Integer originCurrent = balances.get(originAccountId);
            if (originCurrent == null) {
                return Optional.empty();
            }

            int originNext = originCurrent - amount;
            balances.put(originAccountId, originNext);

            int destinationNext = balances.merge(destinationAccountId, amount, Integer::sum);
            return Optional.of(new TransferResult(originNext, destinationNext));
        }
    }

    @Override
    public void reset() {
        balances.clear();
    }
}
