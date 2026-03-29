package com.lab.wiremock;

import com.lab.wiremock.client.ApiClient;
import com.lab.wiremock.model.TransferRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@DisplayName("Transfer API – Testes de Integração")
class TransferIT {

    private static final String TRANSFERS_PATH = "/api/transfers";

    @Test
    @DisplayName("POST /api/transfers → 201 com transferId e status PROCESSING")
    void shouldCreateTransferSuccessfully() {
        
        TransferRequest request = new TransferRequest(
            "ACC-00123",
            "ACC-00456",
            new BigDecimal("500.00"),
            "Pagamento de serviço"
        );

        Response response =
            given(ApiClient.spec())
                .body(request)
            .when()
                .post(TRANSFERS_PATH)
            .then()
                .statusCode(201)
                .extract()
                .response();

        String transferId = response.jsonPath().getString("transferId");
        String status     = response.jsonPath().getString("status");

        assertNotNull(transferId,                         "TransferId não deve ser null");
        assertTrue(transferId.startsWith("TRF-"),        "TransferId deve iniciar com TRF-");
        assertEquals("PROCESSING", status,               "Status inicial deve ser PROCESSING");
    }

    @Test
    @DisplayName("POST /api/transfers com conta sem saldo → 422 INSUFFICIENT_FUNDS")
    void shouldReturn422WhenInsufficientFunds() {
        TransferRequest request = new TransferRequest(
            "BROKE-ACCOUNT",
            "ACC-00456",
            new BigDecimal("9999999.99"),
            "Tentativa sem saldo"
        );

        Response response =
            given(ApiClient.spec())
                .body(request)
            .when()
                .post(TRANSFERS_PATH)
            .then()
                .statusCode(422)
                .extract()
                .response();

        String error = response.jsonPath().getString("error");
        String code  = response.jsonPath().getString("code");

        assertEquals("INSUFFICIENT_FUNDS", error);
        assertEquals("ERR-4221", code);
    }

    @Test
    @DisplayName("POST /api/transfers → response 201 deve conter header Location")
    void shouldHaveLocationHeaderOnCreation() {
        TransferRequest request = new TransferRequest(
            "ACC-00123",
            "ACC-00789",
            new BigDecimal("100.00"),
            "Teste location header"
        );

        String location =
            given(ApiClient.spec())
                .body(request)
            .when()
                .post(TRANSFERS_PATH)
            .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertNotNull(location,                                  "Header Location não deve ser null");
        assertTrue(location.startsWith("/api/transfers/TRF-"),   "Location deve apontar para o recurso");
    }
}