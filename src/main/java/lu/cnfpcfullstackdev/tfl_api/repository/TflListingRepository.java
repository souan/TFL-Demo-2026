package lu.cnfpcfullstackdev.tfl_api.repository;

import lu.cnfpcfullstackdev.tfl_api.entity.ListingStatus;
import lu.cnfpcfullstackdev.tfl_api.entity.TflListing;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TflListingRepository {
    
    // Temporary in-memory storage (replaced by database on Day 2)
    private List<TflListing> listings = new ArrayList<>();
    private Long nextId = 1L;
    
    // Constructor - add sample data
    public TflListingRepository() {
        listings.add(new TflListing(
            nextId++,
            "Fresh Baguettes",
            "10 fresh baguettes from today's batch",
            10,
            LocalDateTime.now().plusHours(6),
            "17:00-18:00",
            ListingStatus.AVAILABLE
        ));
        
        listings.add(new TflListing(
            nextId++,
            "Croissants",
            "Butter croissants, best before end of day",
            15,
            LocalDateTime.now().plusHours(4),
            "16:00-17:00",
            ListingStatus.AVAILABLE
        ));
        
        listings.add(new TflListing(
            nextId++,
            "Sandwich Platter",
            "Assorted sandwiches from lunch service",
            8,
            LocalDateTime.now().plusHours(3),
            "15:00-16:00",
            ListingStatus.CLAIMED
        ));
    }
    
    // Find all listings
    public List<TflListing> findAll() {
        return new ArrayList<>(listings);
    }
    
    // Find by ID
    public Optional<TflListing> findById(Long id) {
        return listings.stream()
                .filter(listing -> listing.getId().equals(id))
                .findFirst();
    }
    
    // Find by status
    public List<TflListing> findByStatus(ListingStatus status) {
        return listings.stream()
                .filter(listing -> listing.getStatus().equals(status))
                .toList();
    }
    
    // Save (create or update)
    public TflListing save(TflListing listing) {
        if (listing.getId() == null) {
            // Create new
            listing.setId(nextId++);
            listings.add(listing);
        } else {
            // Update existing
            deleteById(listing.getId());
            listings.add(listing);
        }
        return listing;
    }
    
    // Check if exists
    public boolean existsById(Long id) {
        return listings.stream()
                .anyMatch(listing -> listing.getId().equals(id));
    }
    
    // Delete by ID
    public void deleteById(Long id) {
        listings.removeIf(listing -> listing.getId().equals(id));
    }

    //Check if title exists
    public boolean existsByTitle(String title){
        return listings
        .stream().anyMatch(listing -> listing.getTitle().equalsIgnoreCase(title));
    }
}