package com.viora.spi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viora.spi.usecase.RegisterAccountUseCase;
import com.viora.spi.usecase.command.RegisterUserCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.viora.spi.client.KeycloakTokenHandler.getTokenWithClientCredentialsFlow;


@Slf4j
public class AccountsApiClient implements RegisterAccountUseCase {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public void registerAccount(RegisterUserCommand registerUserCommand) {

        String url = AccountsApi.BASE_URL + AccountsApi.REGISTER_ACCOUNT;
        HttpClient client = HttpClient.newHttpClient();
        String json = objectMapper.writeValueAsString(registerUserCommand);

        log.info("Sending request for User registration to {} with jsonBody: {}", url, json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getTokenWithClientCredentialsFlow())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Returned response with status code {}", response.statusCode());
    }
}
