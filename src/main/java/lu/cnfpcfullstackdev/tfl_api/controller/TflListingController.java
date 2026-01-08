package lu.cnfpcfullstackdev.tfl_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lu.cnfpcfullstackdev.tfl_api.entity.TflListing;
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
public class TflListingController {

    @Autowired
    private TflListingService listingService;

    // Get All listings
    @GetMapping
    public List<TflListing> getAllListings() {
        return listingService.getAllListings();
    }


    // Get listing by ID
    @GetMapping("/{id}")
    public TflListing getListingById(@PathVariable Long id) {
        return listingService.getListingById(id);
    }

    //POST - Create new Listing
    @PostMapping
    public ResponseEntity<TflListing> createListing(@RequestBody TflListing listing ) {
        TflListing created = listingService.createListing(listing);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //GET - Search by status
    @GetMapping("/search")
    public List<TflListing> getListingByStatus (@RequestParam String status) {
        return listingService.searchByStatus(status);
    }

    // PUT - Update listing
    @PutMapping("/{id}")
    public TflListing updateListing(@PathVariable Long id, @RequestBody TflListing updatedListing) {
        return listingService.updateListing(id, updatedListing);
    }
    
    // DELETE - Remove listing
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
     
}
