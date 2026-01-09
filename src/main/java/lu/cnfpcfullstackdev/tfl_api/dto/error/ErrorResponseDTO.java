package lu.cnfpcfullstackdev.tfl_api.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

//define what our error responses should look like
// DTO = Data Transfert Object (Our custom response)
public class ErrorResponseDTO {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String,String> errors;

    //Constructor
    public ErrorResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with errors ( for validation errors)
    public ErrorResponseDTO(int status, String message, Map<String,String> errors){
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    
    //Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String,String> getErrors() {
        return this.errors;
    }

    public void setErrors(Map<String,String> errors) {
        this.errors = errors;
    }

    
}
