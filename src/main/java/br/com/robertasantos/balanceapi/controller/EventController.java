package br.com.robertasantos.balanceapi.controller;

import br.com.robertasantos.balanceapi.dto.*;
import br.com.robertasantos.balanceapi.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Event", description = "Event processing (deposit, withdraw, transfer)")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/event",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Process event",
            description = "Processes deposit, withdraw or transfer events. Returns 201 with JSON body on success.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EventRequest.class),
                            examples = {
                                    @ExampleObject(name = "deposit", value = "{\"type\":\"deposit\",\"destination\":\"100\",\"amount\":10}"),
                                    @ExampleObject(name = "withdraw", value = "{\"type\":\"withdraw\",\"origin\":\"100\",\"amount\":5}"),
                                    @ExampleObject(name = "transfer", value = "{\"type\":\"transfer\",\"origin\":\"100\",\"destination\":\"300\",\"amount\":15}")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Event processed",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(oneOf = {DepositResponseDto.class, WithdrawResponseDto.class, TransferResponseDto.class}))),
                    @ApiResponse(responseCode = "404", description = "Origin account not found for withdraw/transfer. Body is literal 0.",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    examples = @ExampleObject(value = "0")))
            }
    )
    public ResponseEntity<?> event(@RequestBody EventRequest request) {
        Object result = eventService.process(request);
        return ResponseEntity.status(201).body(result);
    }
}
