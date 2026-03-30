package com.lab.wiremock;

import com.lab.wiremock.client.ApiClient;
import com.lab.wiremock.model.Account;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@DisplayName("Account API – Testes de Integração")
class AccountTest {

    private static final String ACCOUNTS_PATH = "/api/accounts/{id}";

    @Test
    @DisplayName("GET /api/accounts/{id} → 200 com dados corretos")
    void shouldReturnAccountWhenExists() {
        String accountId = "ACC-00123";

        Account account =
            given()
                .spec(ApiClient.spec())
                .pathParam("id", accountId)
            .when()
                .get(ACCOUNTS_PATH)
                // .get("/api/accounts/{id}", accountId) 
            .then()
                .statusCode(200)
                .extract()
                .as(Account.class);

        System.out.println("URL Base: " + System.getProperty("wiremock.base.url"));

        assertNotNull(account,                             "Account não deve ser null");
        assertEquals(accountId,  account.getId(),          "ID deve corresponder ao path");
        assertEquals("ACTIVE",   account.getStatus(),      "Status deve ser ACTIVE");
        assertEquals("BRL",      account.getCurrency(),    "Moeda deve ser BRL");
        assertNotNull(account.getBalance(),                "Balance não pode ser null");
        assertTrue(account.getBalance().doubleValue() > 0, "Balance deve ser positivo");
    }

    @Test
    @DisplayName("GET /api/accounts/not-found → 404 com error body")
    void shouldReturn404WhenAccountDoesNotExist() {
        Response response =
            given()
                .spec(ApiClient.spec())
                .pathParam("id", "not-found")
            .when()
                .get(ACCOUNTS_PATH)
            .then()
                .statusCode(404)
                .extract()
                .response();

        String error = response.jsonPath().getString("error");
        assertEquals("ACCOUNT_NOT_FOUND", error);
    }

    @Test
    @DisplayName("GET /api/accounts/{id} → response deve conter X-Correlation-Id")
    void shouldHaveCorrelationIdInResponse() {
        given(ApiClient.spec())
            .pathParam("id", "ACC-99999")
        .when()
            .get(ACCOUNTS_PATH)
        .then()
            .statusCode(200)
            .header("X-Correlation-Id", org.hamcrest.Matchers.notNullValue());
    }
}