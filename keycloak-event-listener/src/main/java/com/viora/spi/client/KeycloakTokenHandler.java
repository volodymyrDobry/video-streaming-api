package com.viora.spi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static com.viora.spi.keycloak.KeycloakProperties.*;

public class KeycloakTokenHandler {

    @SneakyThrows
    public static String getTokenWithClientCredentialsFlow() {

        String body = "grant_type=client_credentials" +
                "&client_id=" + URLEncoder.encode(KEYCLOAK_CLIENT_ID, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(KEYCLOAK_CLIENT_KEY, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KEYCLOAK_URL + "/realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON to extract access_token
        String json = response.body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(json)
                .get("access_token")
                .asText();
    }
}

