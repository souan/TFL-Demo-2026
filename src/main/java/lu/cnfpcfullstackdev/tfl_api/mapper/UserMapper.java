package lu.cnfpcfullstackdev.tfl_api.mapper;

import lu.cnfpcfullstackdev.tfl_api.dto.request.CreateUserRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.request.UpdateUserRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.response.UserResponseDTO;
import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import lu.cnfpcfullstackdev.tfl_api.entity.UserRole;

public class UserMapper {

    // Convert TflUser entity to UserResponseDTO
    public static UserResponseDTO toResponseDTO(TflUser user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().toString(),
            user.getBusinessName(),
            user.getBusinessAddress(),
            user.getBusinessPhone()
        );
    }

    // Convert CreateUserRequestDTO to TflUser entity (without password hashing)
    // Note: Password hashing should be done in the service layer
    public static TflUser toEntity(CreateUserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        TflUser user = new TflUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        // Password will be set in service layer after hashing
        user.setRole(dto.getRole());

        // Set business fields if role is BUSINESS
        if (dto.getRole() == UserRole.BUSINESS) {
            user.setBusinessName(dto.getBusinessName());
            user.setBusinessAddress(dto.getBusinessAddress());
            user.setBusinessPhone(dto.getBusinessPhone());
        }

        return user;
    }

    // Update existing TflUser entity from UpdateUserRequestDTO
    public static void updateEntity(TflUser user, UpdateUserRequestDTO dto) {
        if (user == null || dto == null) {
            return;
        }

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        // Update business fields
        if (dto.getBusinessName() != null) {
            user.setBusinessName(dto.getBusinessName());
        }

        if (dto.getBusinessAddress() != null) {
            user.setBusinessAddress(dto.getBusinessAddress());
        }

        if (dto.getBusinessPhone() != null) {
            user.setBusinessPhone(dto.getBusinessPhone());
        }
    }
}
