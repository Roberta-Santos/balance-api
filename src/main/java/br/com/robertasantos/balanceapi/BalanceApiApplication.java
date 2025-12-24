package br.com.robertasantos.balanceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BalanceApiApplication {

    /**
     * Exposed for testability (coverage) and to allow controlled lifecycle in tests.
     */
    public static ConfigurableApplicationContext run(String[] args) {
        return SpringApplication.run(BalanceApiApplication.class, args);
    }

    public static void main(String[] args) {
        // When balanceapi.autoClose=true, we start and immediately close the context.
        // This is used by unit tests to cover the main method without leaving a running server.
        if (Boolean.getBoolean("balanceapi.autoClose")) {
            try (ConfigurableApplicationContext ctx = run(args)) {
                // no-op
            }
            return;
        }
        // Shortcut for tests: start and immediately close the context when
        // `balanceapi.autoCloseOnRun` is set. This lets tests exercise the
        // `run(args)` path without leaving a running server.
        if (Boolean.getBoolean("balanceapi.autoCloseOnRun")) {
            try (ConfigurableApplicationContext ctx = run(args)) {
                // no-op
            }
            return;
        }
        run(args);
    }
}
