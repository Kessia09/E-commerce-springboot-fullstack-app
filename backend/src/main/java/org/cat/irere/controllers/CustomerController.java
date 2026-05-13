package org.cat.irere.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cat.irere.dto.ApiResponse;
import org.cat.irere.dto.CustomerDTO;
import org.cat.irere.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Customer", description = "Customer profile API")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get current user profile", description = "Retrieves the profile of the currently logged-in user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If user is not authenticated or it's an anonymous user
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.ok(ApiResponse.success("No authenticated user", null));
        }

        String email = authentication.getName();
        Optional<CustomerDTO> customerDTO = customerService.getCustomerByEmail(email);

        if (customerDTO.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", customerDTO.get()));
        } else {
            return ResponseEntity.ok(ApiResponse.success("User not found", null));
        }
    }
}