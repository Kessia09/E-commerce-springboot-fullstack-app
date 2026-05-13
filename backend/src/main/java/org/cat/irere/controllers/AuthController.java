package org.cat.irere.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cat.irere.dto.ApiResponse;
import org.cat.irere.dto.AuthResponse;
import org.cat.irere.dto.CustomerDTO;
import org.cat.irere.dto.LoginDTO;
import org.cat.irere.dto.RegisterDTO;
import org.cat.irere.security.JwtTokenProvider;
import org.cat.irere.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final CustomerService customerService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(CustomerService customerService, JwtTokenProvider jwtTokenProvider) {
        this.customerService = customerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Parameter(description = "Registration details", required = true) @RequestBody RegisterDTO registerDTO) {
        try {
            CustomerDTO customerDTO = customerService.register(registerDTO);
            String token = jwtTokenProvider.createToken(customerDTO.getEmail());

            AuthResponse authResponse = new AuthResponse(token, customerDTO);
            return ResponseEntity.ok(ApiResponse.success("Registration successful", authResponse));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during registration"));
        }
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Parameter(description = "Login credentials", required = true) @RequestBody LoginDTO loginDTO) {
        try {
            CustomerDTO customerDTO = customerService.login(loginDTO);
            String token = jwtTokenProvider.createToken(customerDTO.getEmail());

            AuthResponse authResponse = new AuthResponse(token, customerDTO);
            return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during login"));
        }
    }
}