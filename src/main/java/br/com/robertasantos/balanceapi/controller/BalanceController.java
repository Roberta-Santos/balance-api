package br.com.robertasantos.balanceapi.controller;

import br.com.robertasantos.balanceapi.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "Balance", description = "Balance queries")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(value = "/balance", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Get balance", description = "Returns account balance as plain text. If account does not exist, returns 404 with body '0'.")
    public ResponseEntity<String> getBalance(
            @Parameter(description = "Account id", required = true, example = "100")
            @RequestParam("account_id") String accountId
    ) {
        Optional<Integer> balance = balanceService.getBalance(accountId);
        if (balance.isEmpty()) {
            return ResponseEntity.status(404).body("0");
        }
        return ResponseEntity.ok(Integer.toString(balance.get()));
    }
}
