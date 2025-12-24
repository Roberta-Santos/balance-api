package br.com.robertasantos.balanceapi.unit.service;

import br.com.robertasantos.balanceapi.dto.*;
import br.com.robertasantos.balanceapi.exception.AccountNotFoundException;
import br.com.robertasantos.balanceapi.repository.AccountRepositoryImpl;
import br.com.robertasantos.balanceapi.service.EventService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    @Test
    void deposit_createsDestinationAccount() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        EventService service = new EventService(repo);

        Object response = service.process(new EventRequest("deposit", null, "100", 10));
        assertInstanceOf(DepositResponseDto.class, response);

        DepositResponseDto dto = (DepositResponseDto) response;
        assertEquals("100", dto.getDestination().getId());
        assertEquals(10, dto.getDestination().getBalance());
        assertEquals(10, repo.findBalance("100").orElseThrow());
    }

    @Test
    void deposit_mergesWhenDestinationExists() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 5);

        EventService service = new EventService(repo);
        Object resp = service.process(new br.com.robertasantos.balanceapi.dto.EventRequest("deposit", null, "100", 10));
        assertInstanceOf(DepositResponseDto.class, resp);
        DepositResponseDto dto = (DepositResponseDto) resp;
        assertEquals("100", dto.getDestination().getId());
        assertEquals(15, dto.getDestination().getBalance());
    }

    @Test
    void withdraw_throwsWhenOriginDoesNotExist() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(AccountNotFoundException.class,
                () -> service.process(new EventRequest("withdraw", "100", null, 5)));
    }

    @Test
    void withdraw_updatesOriginBalance() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 20);
        EventService service = new EventService(repo);

        Object response = service.process(new EventRequest("withdraw", "100", null, 7));
        assertInstanceOf(WithdrawResponseDto.class, response);

        WithdrawResponseDto dto = (WithdrawResponseDto) response;
        assertEquals("100", dto.getOrigin().getId());
        assertEquals(13, dto.getOrigin().getBalance());
    }

    @Test
    void transfer_throwsWhenOriginDoesNotExist() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(AccountNotFoundException.class,
                () -> service.process(new EventRequest("transfer", "100", "300", 10)));
    }

    @Test
    void transfer_updatesOriginAndDestination() {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.deposit("100", 20);
        EventService service = new EventService(repo);

        Object response = service.process(new EventRequest("transfer", "100", "300", 15));
        assertInstanceOf(TransferResponseDto.class, response);

        TransferResponseDto dto = (TransferResponseDto) response;
        assertEquals("100", dto.getOrigin().getId());
        assertEquals(5, dto.getOrigin().getBalance());
        assertEquals("300", dto.getDestination().getId());
        assertEquals(15, dto.getDestination().getBalance());
    }

    @Test
    void process_throwsForUnsupportedType() {
        EventService service = new EventService(new AccountRepositoryImpl());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("unknown", null, null, 1)));
        assertTrue(ex.getMessage().contains("Unsupported event type"));
    }

    @Test
    void validate_rejectsNullRequest() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class, () -> service.process(null));
    }

    @Test
    void validate_rejectsMissingType() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest(null, null, "100", 10)));
    }

    @Test
    void validate_rejectsBlankType() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("   ", null, "100", 10)));
    }

    @Test
    void validate_rejectsBlankDestinationForDeposit() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("deposit", null, "   ", 10)));
    }

    @Test
    void validate_rejectsBlankOriginForWithdraw() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("withdraw", "  ", null, 10)));
    }

    @Test
    void validate_rejectsBlankOriginForTransfer() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("transfer", "", "300", 10)));
    }

    @Test
    void validate_rejectsNullOriginForTransfer() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("transfer", null, "300", 10)));
    }

    @Test
    void validate_rejectsBlankDestinationForTransfer() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("transfer", "100", "   ", 10)));
    }

    @Test
    void validate_rejectsMissingAmount() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("deposit", null, "100", null)));
    }

    @Test
    void validate_rejectsNegativeAmount() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("deposit", null, "100", -1)));
    }

    @Test
    void validate_requiresDestinationForDeposit() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("deposit", null, null, 10)));
    }

    @Test
    void validate_requiresOriginForWithdraw() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("withdraw", null, null, 10)));
    }

    @Test
    void validate_requiresOriginAndDestinationForTransfer() {
        EventService service = new EventService(new AccountRepositoryImpl());
        assertThrows(IllegalArgumentException.class,
                () -> service.process(new EventRequest("transfer", "100", null, 10)));
    }
}
