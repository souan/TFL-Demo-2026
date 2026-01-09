package lu.cnfpcfullstackdev.tfl_api.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "tfl_user")
public class TflUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password; //OMG , will be hashed in Day 3!

    @Enumerated(EnumType.STRING)
    private UserRole role;


    //Business-specific fields (only used if role = BUSINESS)
    private String businessName;
    private String businessAddress;
    private String businessPhone;

    @OneToMany(mappedBy="business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TflListing> listings = new ArrayList<>();

    // Empty Constructor
    public TflUser() {
    }
    

    // Getters and Setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getBusinessName() {
        return this.businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return this.businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessPhone() {
        return this.businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public List<TflListing> getListings() {
        return this.listings;
    }

    public void setListings(List<TflListing> listings) {
        this.listings = listings;
    }
    
}
