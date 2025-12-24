package br.com.robertasantos.balanceapi.service;

import br.com.robertasantos.balanceapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class ResetService {

    private final AccountRepository repository;

    public ResetService(AccountRepository repository) {
        this.repository = repository;
    }

    public void reset() {
        repository.reset();
    }
}
