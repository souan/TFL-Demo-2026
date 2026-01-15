package lu.cnfpcfullstackdev.tfl_api.service;

import lu.cnfpcfullstackdev.tfl_api.dto.request.CreateListingRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.request.UpdateListingRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.response.ListingResponseDTO;
import lu.cnfpcfullstackdev.tfl_api.entity.ListingStatus;
import lu.cnfpcfullstackdev.tfl_api.entity.TflListing;
import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import lu.cnfpcfullstackdev.tfl_api.entity.UserRole;
import lu.cnfpcfullstackdev.tfl_api.exception.ResourceNotFoundException;
import lu.cnfpcfullstackdev.tfl_api.mapper.ListingMapper;
import lu.cnfpcfullstackdev.tfl_api.repository.TflListingRepository;
import lu.cnfpcfullstackdev.tfl_api.repository.TflUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TflListingService {
    
    @Autowired
    private TflListingRepository repository;

    @Autowired
    private TflUserRepository userRepository;
    
    // Get all listings
    public List<ListingResponseDTO> getAllListings() {
        return repository.findAll()
                    .stream()
                    .map(ListingMapper::toResponseDTO)
                    .collect(Collectors.toList());
    }
    
    // Get listing by ID
    public ListingResponseDTO getListingById(Long id) {
        TflListing listing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", id));          
        return ListingMapper.toResponseDTO(listing);
    }
    
    // Create new listing
    public ListingResponseDTO createListing(CreateListingRequestDTO dto, Long businessId) {

        //Find corresponding business TflUser
        TflUser business = userRepository.findById(businessId)
                            .orElseThrow(() -> new ResourceNotFoundException("TflUser (Business)", businessId));

        // Verify it's actually a business
        if(business.getRole() != UserRole.BUSINESS){
            throw new RuntimeException("User with id " + businessId + " is not a business");
        }

        //Convert DTO to entity
        TflListing listing = ListingMapper.toEntity(dto);

        //Set the relationship
        listing.setBusiness(business);


        //Check for duplicate
        // if(repository.existsByTitle(listing.getTitle())){
        //     throw new DuplicateResourceException("Listing", "Title", listing.getTitle());
        // }

        // Business logic: Always set status to AVAILABLE when creating
        listing.setStatus(ListingStatus.AVAILABLE);

        TflListing savedListing = repository.save(listing);

        return ListingMapper.toResponseDTO(savedListing);
    }
    
    // Search by status
    public List<ListingResponseDTO> searchByStatus(String status) {
        if (status != null) {
            ListingStatus listingStatus = ListingStatus.valueOf(status.toUpperCase());
            return repository.findByStatus(listingStatus)
                    .stream()
                    .map(ListingMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }
        return repository.findAll()
                    .stream()
                    .map(ListingMapper::toResponseDTO)
                    .collect(Collectors.toList());
    }
    
    // PUT - receives RequestDTO, returns ResponseDTO
    public ListingResponseDTO updateListing(Long id, UpdateListingRequestDTO dto, Long businessId) {
        TflListing listing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", id));

        // Authorization: Verify the listing belongs to the authenticated business
        if (!listing.getBusiness().getId().equals(businessId)) {
            throw new RuntimeException("You are not authorized to update this listing");
        }

        // Update entity from DTO
        ListingMapper.updateEntity(listing, dto);

        // Save and return
        TflListing updated = repository.save(listing);
        return ListingMapper.toResponseDTO(updated);
    }
    
    // Delete listing
    public void deleteListing(Long id, Long businessId) {
        TflListing listing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", id));

        // Authorization: Verify the listing belongs to the authenticated business
        if (!listing.getBusiness().getId().equals(businessId)) {
            throw new RuntimeException("You are not authorized to delete this listing");
        }

        repository.deleteById(id);
    }
}