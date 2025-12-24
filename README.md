# Balance API (In-Memory)

Simple in-memory API for managing account balances.

Main endpoints:

- `GET /balance?account_id=...` — returns the account balance
- `POST /event` — processes events (deposit, withdraw, transfer)
- `POST /reset` — resets in-memory state

Persistence: fully in-memory (no database).

Tech stack

- Java 17
- Spring Boot 3
- Maven
- springdoc-openapi (Swagger UI)

Run locally

```bash
mvn spring-boot:run
```

The server listens on port `8080` by default.

Swagger / OpenAPI

- OpenAPI JSON: `/v3/api-docs`
- Swagger UI: `/swagger-ui.html`

Content negotiation notes

- The `GET /balance` and `POST /reset` endpoints produce `text/plain` but are also declared to accept `application/json` via content negotiation (both media types are supported). This prevents `406 Not Acceptable` when the client sends `Accept: application/json`.
- The `POST /event` endpoint produces `application/json`.

Endpoints behavior

POST /reset
- Clears all in-memory state.
- Response: `200 OK` with body `OK` (content type depends on `Accept`).

GET /balance?account_id=100
- Returns the balance as plain text when requested.
- If the account exists: `200 OK` with body like `10` (text) or JSON when the client requests JSON.
- If the account does not exist: `404 Not Found` with body `0`.

POST /event
- Processes JSON events:
	- Deposit: `{"type":"deposit","destination":"100","amount":10}`
	- Withdraw: `{"type":"withdraw","origin":"100","amount":5}`
	- Transfer: `{"type":"transfer","origin":"100","destination":"300","amount":15}`
- On success: `201 Created` with a JSON body describing affected accounts.
- If the origin account does not exist (withdraw/transfer): `404 Not Found` with body `0`.

Example curl commands

```bash
# reset (requesting text/plain)
curl -X POST -H "Accept: text/plain" http://localhost:8080/reset

# get balance as plain text
curl -i -H "Accept: text/plain" "http://localhost:8080/balance?account_id=100"

# get balance as JSON (also supported)
curl -i -H "Accept: application/json" "http://localhost:8080/balance?account_id=100"

# create account via event (JSON)
curl -X POST -H "Content-Type: application/json" -d '{"type":"deposit","destination":"100","amount":10}' http://localhost:8080/event
```

Tests and coverage

Unit and integration tests are included (JUnit + Spring MockMvc). JaCoCo reports are generated under `target/jacoco-*`.