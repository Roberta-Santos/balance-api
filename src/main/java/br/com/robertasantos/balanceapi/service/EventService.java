package br.com.robertasantos.balanceapi.service;

import br.com.robertasantos.balanceapi.dto.*;
import br.com.robertasantos.balanceapi.exception.AccountNotFoundException;
import br.com.robertasantos.balanceapi.repository.AccountRepository;
import br.com.robertasantos.balanceapi.repository.AccountRepository.TransferResult;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final AccountRepository repository;

    public EventService(AccountRepository repository) {
        this.repository = repository;
    }

    public Object process(EventRequest request) {
        validate(request);

        String type = request.getType();
        int amount = request.getAmount();

        return switch (type) {
            case "deposit" -> deposit(request.getDestination(), amount);
            case "withdraw" -> withdraw(request.getOrigin(), amount);
            case "transfer" -> transfer(request.getOrigin(), request.getDestination(), amount);
            default -> throw new IllegalArgumentException("Unsupported event type: " + type);
        };
    }

    private DepositResponseDto deposit(String destinationAccountId, int amount) {
        int newBalance = repository.deposit(destinationAccountId, amount);
        return new DepositResponseDto(new AccountDto(destinationAccountId, newBalance));
    }

    private WithdrawResponseDto withdraw(String originAccountId, int amount) {
        Integer newBalance = repository.withdraw(originAccountId, amount)
                .orElseThrow(AccountNotFoundException::new);
        return new WithdrawResponseDto(new AccountDto(originAccountId, newBalance));
    }

    private TransferResponseDto transfer(String originAccountId, String destinationAccountId, int amount) {
        TransferResult result = repository.transfer(originAccountId, destinationAccountId, amount)
                .orElseThrow(AccountNotFoundException::new);

        AccountDto origin = new AccountDto(originAccountId, result.originBalance());
        AccountDto destination = new AccountDto(destinationAccountId, result.destinationBalance());
        return new TransferResponseDto(origin, destination);
    }

    private void validate(EventRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Event request cannot be null");
        }
        if (request.getType() == null || request.getType().isBlank()) {
            throw new IllegalArgumentException("Event type is required");
        }
        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (request.getAmount() < 0) {
            throw new IllegalArgumentException("Amount must be >= 0");
        }

        switch (request.getType()) {
            case "deposit" -> {
                if (request.getDestination() == null || request.getDestination().isBlank()) {
                    throw new IllegalArgumentException("Destination is required for deposit");
                }
            }
            case "withdraw" -> {
                if (request.getOrigin() == null || request.getOrigin().isBlank()) {
                    throw new IllegalArgumentException("Origin is required for withdraw");
                }
            }
            case "transfer" -> {
                if (request.getOrigin() == null || request.getOrigin().isBlank()) {
                    throw new IllegalArgumentException("Origin is required for transfer");
                }
                if (request.getDestination() == null || request.getDestination().isBlank()) {
                    throw new IllegalArgumentException("Destination is required for transfer");
                }
            }
            default -> {
                // Let process() throw Unsupported type with clearer message.
            }
        }
    }
}
