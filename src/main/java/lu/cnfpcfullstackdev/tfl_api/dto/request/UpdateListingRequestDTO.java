package lu.cnfpcfullstackdev.tfl_api.dto.request;

import java.time.LocalDateTime;

public class UpdateListingRequestDTO {
    
    private String title;
    private String description;
    private Integer quantity;
    private LocalDateTime expiryDate;
    private String pickupTime;
    
    // No 'id' - comes from URL path (@PathVariable)
    // No 'status' - handled separately
    // No 'businessId' - can't change owner
    
    // Empty constructor
    public UpdateListingRequestDTO() {
    }

    // Getters and Setters...

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPickupTime() {
        return this.pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

}
