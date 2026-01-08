package lu.cnfpcfullstackdev.tfl_api.service;

import lu.cnfpcfullstackdev.tfl_api.entity.ListingStatus;
import lu.cnfpcfullstackdev.tfl_api.entity.TflListing;
import lu.cnfpcfullstackdev.tfl_api.exception.DuplicateResourceException;
import lu.cnfpcfullstackdev.tfl_api.exception.ResourceNotFoundException;
import lu.cnfpcfullstackdev.tfl_api.repository.TflListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TflListingService {
    
    @Autowired
    private TflListingRepository repository;
    
    // Get all listings
    public List<TflListing> getAllListings() {
        return repository.findAll();
    }
    
    // Get listing by ID
    public TflListing getListingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", id));
    }
    
    // Create new listing
    public TflListing createListing(TflListing listing) {

        //Check for duplicate
        if(repository.existsByTitle(listing.getTitle())){
            throw new DuplicateResourceException("Listing", "Title", listing.getTitle());
        }

        // Business logic: Always set status to AVAILABLE when creating
        listing.setStatus(ListingStatus.AVAILABLE);
        return repository.save(listing);
    }
    
    // Search by status
    public List<TflListing> searchByStatus(String status) {
        if (status != null) {
            ListingStatus listingStatus = ListingStatus.valueOf(status.toUpperCase());
            return repository.findByStatus(listingStatus);
        }
        return repository.findAll();
    }
    
    // Update listing
    public TflListing updateListing(Long id, TflListing updatedListing) {
        return repository.findById(id)
            .map(listing -> {
                listing.setTitle(updatedListing.getTitle());
                listing.setDescription(updatedListing.getDescription());
                listing.setQuantity(updatedListing.getQuantity());
                listing.setExpiryDate(updatedListing.getExpiryDate());
                listing.setPickupTime(updatedListing.getPickupTime());
                // Note: We're NOT updating status here - business decision!
                return repository.save(listing);
            })
            .orElseThrow(() -> new ResourceNotFoundException("Listing", id));
    }
    
    // Delete listing
    public void deleteListing(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Listing", id);
        }
        repository.deleteById(id);
    }
}