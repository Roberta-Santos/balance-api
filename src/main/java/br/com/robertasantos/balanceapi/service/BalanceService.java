package br.com.robertasantos.balanceapi.service;

import br.com.robertasantos.balanceapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BalanceService {

    private final AccountRepository repository;

    public BalanceService(AccountRepository repository) {
        this.repository = repository;
    }

    public Optional<Integer> getBalance(String accountId) {
        return repository.findBalance(accountId);
    }
}
