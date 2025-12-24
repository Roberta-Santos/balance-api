package br.com.robertasantos.balanceapi.controller;

import br.com.robertasantos.balanceapi.service.ResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Reset", description = "Reset in-memory state (used by test suite)")
public class ResetController {

    private final ResetService resetService;

    public ResetController(ResetService resetService) {
        this.resetService = resetService;
    }

    @PostMapping(value = "/reset", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Reset state", description = "Clears all accounts and balances. Returns plain text OK.")
    public ResponseEntity<String> reset() {
        resetService.reset();
        return ResponseEntity.ok("OK");
    }
}
