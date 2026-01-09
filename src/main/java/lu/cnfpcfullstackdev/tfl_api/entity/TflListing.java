package lu.cnfpcfullstackdev.tfl_api.entity;
 
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
public class TflListing {
    //Data fields
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer quantity;
    private LocalDateTime expiryDate;
    private String pickupTime;

    @Enumerated(EnumType.STRING)
    private ListingStatus status;

    // Relationship to TflUser (Business)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "business_id")  //created the FK
    private TflUser business;


    // Empty constructor (required for JSON serialization)
    public TflListing() {
    }

    // Constructor with all fields
    public TflListing(Long id, String title, String description, 
                      Integer quantity, LocalDateTime expiryDate, 
                      String pickupTime, ListingStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.pickupTime = pickupTime;
        this.status = status;
    }

    // Getters
    public Long getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        return this.description;
    }
    public Integer getQuantity() {
        return this.quantity;
    }
    public LocalDateTime getExpiryDate() {
        return this.expiryDate;
    }
    public String getPickupTime() {
        return this.pickupTime;
    }
    public ListingStatus getStatus() {
        return this.status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }
    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TflListing{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                '}';
    }

    public TflUser getBusiness() {
        return this.business;
    }

    public void setBusiness(TflUser business) {
        this.business = business;
    }

}