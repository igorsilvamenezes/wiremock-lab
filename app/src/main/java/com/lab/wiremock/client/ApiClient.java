package com.lab.wiremock.client;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class ApiClient {

    private static final RequestSpecification SPEC;

    static {
        String baseUrl = System.getProperty(
            "wiremock.base.url", 
            "http://localhost:8080"
        );
        
        SPEC = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath("") // ISSO LIMPA A DUPLICAÇÃO
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("X-Client-Id", "wiremock-lab")
                .log(LogDetail.ALL)
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static RequestSpecification spec() {
        return SPEC;
    }

    private ApiClient() {}
}