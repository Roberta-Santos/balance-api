package br.com.robertasantos.balanceapi.unit.exception;

import br.com.robertasantos.balanceapi.exception.AccountNotFoundException;
import br.com.robertasantos.balanceapi.exception.RestExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestExceptionHandlerTest {

    @Test
    void handleAccountNotFound_returns404BodyZero() {
        RestExceptionHandler h = new RestExceptionHandler();
        ResponseEntity<String> r = h.handleAccountNotFound(new AccountNotFoundException());
        assertEquals(404, r.getStatusCode().value());
        assertEquals("0", r.getBody());
    }

    @Test
    void handleBadRequest_returnsMessage() {
        RestExceptionHandler h = new RestExceptionHandler();
        ResponseEntity<String> r = h.handleBadRequest(new IllegalArgumentException("bad"));
        assertEquals(400, r.getStatusCode().value());
        assertEquals("bad", r.getBody());
    }
}
