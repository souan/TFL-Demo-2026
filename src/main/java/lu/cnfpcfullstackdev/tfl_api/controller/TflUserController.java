package lu.cnfpcfullstackdev.tfl_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lu.cnfpcfullstackdev.tfl_api.dto.request.CreateUserRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.request.UpdateUserRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.response.UserResponseDTO;
import lu.cnfpcfullstackdev.tfl_api.service.TflUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "Admin endpoints for managing users. Most endpoints require ADMIN role.")
public class TflUserController {

    @Autowired
    private TflUserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all users",
        description = "Retrieve all registered users. Requires ADMIN role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved all users",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Requires ADMIN role",
            content = @Content
        )
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieve a specific user by their ID. Requires ADMIN role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Requires ADMIN role",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/businesses")
    @Operation(
        summary = "Get all business users",
        description = "Retrieve all users with BUSINESS role. Accessible to all authenticated users (used for listings dropdown)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved all businesses",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        )
    })
    public ResponseEntity<List<UserResponseDTO>> getAllBusinesses() {
        return ResponseEntity.ok(userService.getAllBusinesses());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create a new user",
        description = "Create a new user account. Requires ADMIN role. Password will be hashed before storage."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input - validation errors or duplicate username/email",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Requires ADMIN role",
            content = @Content
        )
    })
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO dto) {
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update a user",
        description = "Update an existing user's information. Requires ADMIN role. Password updates are not supported through this endpoint."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input - validation errors or duplicate username/email",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Requires ADMIN role",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete a user",
        description = "Delete a user account. Requires ADMIN role. This will also delete all associated listings if the user is a BUSINESS."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Requires ADMIN role",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}