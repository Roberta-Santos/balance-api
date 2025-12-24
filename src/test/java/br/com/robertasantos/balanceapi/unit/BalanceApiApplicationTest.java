package br.com.robertasantos.balanceapi.unit;

import br.com.robertasantos.balanceapi.BalanceApiApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.context.ConfigurableApplicationContext;

class BalanceApiApplicationTest {

    @Test
    void main_canAutoCloseInTests() {
        System.setProperty("balanceapi.autoClose", "true");
        assertDoesNotThrow(() -> BalanceApiApplication.main(new String[] { "--server.port=0" }));
    }

    @Test
    void run_and_close_context() {
        try (ConfigurableApplicationContext ctx = BalanceApiApplication.run(new String[] { "--server.port=0" })) {
            assertNotNull(ctx);
        }
    }

    @Test
    void main_run_isAutoCloseOnRun() {
        System.setProperty("balanceapi.autoCloseOnRun", "true");
        assertDoesNotThrow(() -> BalanceApiApplication.main(new String[] { "--server.port=0" }));
    }
}
