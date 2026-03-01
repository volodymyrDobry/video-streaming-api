package com.viora.identityaccess.api.docs;

import com.viora.identityaccess.api.request.AdminCreateAccountRequest;
import com.viora.identityaccess.api.request.KeycloakCreateAccountRequest;
import com.viora.identityaccess.domain.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account registration", description = "Admin account management APIs")
@RequestMapping("/api/v1/admin/accounts")
public interface AccountApi {

    @Operation(
            summary = "Create account via Keycloak",
            description = "This endpoint is made only for keycloak usage (will be replaced by queue soon)"
    )
    @ApiResponse(responseCode = "201", description = "Account successfully created")
    @ApiResponse(responseCode = "400", description = "Some fields from request weren't valid")
    @PostMapping("/keycloak")
    ResponseEntity<Account> register(
            @Valid @RequestBody KeycloakCreateAccountRequest request
    );

    @Operation(
            summary = "Create account manually",
            description = "Creates a new account by admin"
    )
    @ApiResponse(responseCode = "201", description = "Account successfully created")
    @ApiResponse(responseCode = "400", description = "Some fields from request weren't valid")
    @PostMapping
    ResponseEntity<Account> register(
            @Valid @RequestBody AdminCreateAccountRequest request
    );
}
