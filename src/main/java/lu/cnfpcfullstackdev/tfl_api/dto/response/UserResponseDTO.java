package lu.cnfpcfullstackdev.tfl_api.dto.response;

public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private String businessName;
    private String businessAddress;
    private String businessPhone;

    // Empty constructor
    public UserResponseDTO() {
    }

    // Full constructor
    public UserResponseDTO(Long id, String username, String email, String role,
                          String businessName, String businessAddress, String businessPhone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.businessPhone = businessPhone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }
}
