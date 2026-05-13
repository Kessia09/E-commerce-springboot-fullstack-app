package org.cat.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cat.dto.ApiResponse;
import org.cat.dto.AuthResponse;
import org.cat.dto.CustomerDTO;
import org.cat.dto.LoginDTO;
import org.cat.dto.RegisterDTO;
import org.cat.security.JwtTokenProvider;
import org.cat.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller responsible for user authentication.
 * Handles user registration and login, and issues JWT tokens for secure access.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    // Service layer handling customer business logic (registration/login)
    private final CustomerService customerService;

    // Utility class for generating JWT tokens
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor-based dependency injection
     */
    public AuthController(CustomerService customerService, JwtTokenProvider jwtTokenProvider) {
        this.customerService = customerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Register a new user account
     * Creates a customer and returns authentication details including JWT token
     */
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Parameter(description = "Registration details", required = true)
            @RequestBody RegisterDTO registerDTO) {

        try {
            // Register user via service layer
            CustomerDTO customerDTO = customerService.register(registerDTO);

            // Generate JWT token for the newly registered user
            String token = jwtTokenProvider.createToken(customerDTO.getEmail());

            // Combine token + user data into response object
            AuthResponse authResponse = new AuthResponse(token, customerDTO);

            return ResponseEntity.ok(ApiResponse.success("Registration successful", authResponse));

        } catch (IllegalArgumentException e) {
            // Handle validation errors (e.g., email already exists)
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            // Log unexpected errors (should ideally use logger instead of System.out)
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during registration"));
        }
    }

    /**
     * Authenticate an existing user
     * Validates credentials and returns a JWT token if successful
     */
    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Parameter(description = "Login credentials", required = true)
            @RequestBody LoginDTO loginDTO) {

        try {
            // Authenticate user credentials
            CustomerDTO customerDTO = customerService.login(loginDTO);

            // Generate JWT token after successful authentication
            String token = jwtTokenProvider.createToken(customerDTO.getEmail());

            // Return token + user info
            AuthResponse authResponse = new AuthResponse(token, customerDTO);

            return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));

        } catch (IllegalArgumentException e) {
            // Handle invalid login credentials
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            // Handle unexpected server errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during login"));
        }
    }
}
