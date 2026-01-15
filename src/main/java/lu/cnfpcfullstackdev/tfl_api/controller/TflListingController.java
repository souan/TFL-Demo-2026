package lu.cnfpcfullstackdev.tfl_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lu.cnfpcfullstackdev.tfl_api.dto.request.CreateListingRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.request.UpdateListingRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.response.ListingResponseDTO;
import lu.cnfpcfullstackdev.tfl_api.entity.TflListing;
import lu.cnfpcfullstackdev.tfl_api.security.UserPrincipal;
import lu.cnfpcfullstackdev.tfl_api.service.TflListingService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/listings")
@Tag(name = "Food Listings", description = "Manage surplus food listings from businesses")
public class TflListingController {

    @Autowired
    private TflListingService listingService;

    @GetMapping
    @Operation(
        summary = "Get all food listings",
        description = "Retrieve all available food listings. No authentication required."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved all listings",
            content = @Content(schema = @Schema(implementation = ListingResponseDTO.class))
        )
    })
    public ResponseEntity<List<ListingResponseDTO>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get listing by ID",
        description = "Retrieve a specific food listing by its ID. No authentication required."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listing found",
            content = @Content(schema = @Schema(implementation = ListingResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Listing not found",
            content = @Content
        )
    })
    public ListingResponseDTO getListingById(
            @Parameter(description = "ID of the listing to retrieve", required = true)
            @PathVariable Long id) {
        return listingService.getListingById(id);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search listings by status",
        description = "Filter food listings by their status (AVAILABLE, RESERVED, COLLECTED). No authentication required."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(schema = @Schema(implementation = ListingResponseDTO.class))
        )
    })
    public List<ListingResponseDTO> getListingByStatus(
            @Parameter(description = "Status to filter by (AVAILABLE, RESERVED, COLLECTED)", required = true)
            @RequestParam String status) {
        return listingService.searchByStatus(status);
    }

    @PostMapping
    @PreAuthorize("hasRole('BUSINESS')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Create a new food listing",
        description = "Create a new surplus food listing. Only BUSINESS users can create listings. The listing will be associated with the authenticated business."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Listing created successfully",
            content = @Content(schema = @Schema(implementation = ListingResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input - validation errors",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Only BUSINESS users can create listings",
            content = @Content
        )
    })
    public ResponseEntity<ListingResponseDTO> createListing(
                            @Valid @RequestBody CreateListingRequestDTO dto) {

        Long businessId = UserPrincipal.getCurrentUserId();
        ListingResponseDTO created = listingService.createListing(dto, businessId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BUSINESS')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Update a food listing",
        description = "Update an existing food listing. Only the business that created the listing can update it."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listing updated successfully",
            content = @Content(schema = @Schema(implementation = ListingResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input - validation errors",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - You can only update your own listings",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Listing not found",
            content = @Content
        )
    })
    public ListingResponseDTO updateListing(
        @Parameter(description = "ID of the listing to update", required = true)
        @PathVariable Long id,
        @Valid @RequestBody UpdateListingRequestDTO updatedListing) {

        Long businessId = UserPrincipal.getCurrentUserId();
        return listingService.updateListing(id, updatedListing, businessId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BUSINESS')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Delete a food listing",
        description = "Delete an existing food listing. Only the business that created the listing can delete it."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Listing deleted successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not authenticated - JWT token missing or invalid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - You can only delete your own listings",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Listing not found",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteListing(
            @Parameter(description = "ID of the listing to delete", required = true)
            @PathVariable Long id) {
        Long businessId = UserPrincipal.getCurrentUserId();
        listingService.deleteListing(id, businessId);
        return ResponseEntity.noContent().build();
    }
     
}
