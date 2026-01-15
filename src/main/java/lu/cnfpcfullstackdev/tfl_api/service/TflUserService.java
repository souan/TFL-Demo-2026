package lu.cnfpcfullstackdev.tfl_api.service;

import lu.cnfpcfullstackdev.tfl_api.dto.request.CreateUserRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.request.UpdateUserRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.response.UserResponseDTO;
import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import lu.cnfpcfullstackdev.tfl_api.entity.UserRole;
import lu.cnfpcfullstackdev.tfl_api.exception.DuplicateResourceException;
import lu.cnfpcfullstackdev.tfl_api.exception.ResourceNotFoundException;
import lu.cnfpcfullstackdev.tfl_api.mapper.UserMapper;
import lu.cnfpcfullstackdev.tfl_api.repository.TflUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TflUserService {

    @Autowired
    private TflUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users - returns DTOs without passwords
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Get user by ID - returns DTO without password
    public UserResponseDTO getUserById(Long id) {
        TflUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return UserMapper.toResponseDTO(user);
    }

    // Get all businesses (useful for dropdown menus)
    public List<UserResponseDTO> getAllBusinesses() {
        return userRepository.findByRole(UserRole.BUSINESS)
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Create user - ADMIN only operation
    public UserResponseDTO createUser(CreateUserRequestDTO dto) {
        // Check if username already exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("User", "Username", dto.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("User", "Email", dto.getEmail());
        }

        // Convert DTO to entity
        TflUser user = UserMapper.toEntity(dto);

        // Hash password before saving
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Save user
        TflUser savedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }

    // Update user - ADMIN only operation
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO dto) {
        TflUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        // Check for username conflict if username is being changed
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new DuplicateResourceException("User", "Username", dto.getUsername());
            }
        }

        // Check for email conflict if email is being changed
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("User", "Email", dto.getEmail());
            }
        }

        // Update entity from DTO
        UserMapper.updateEntity(user, dto);

        // Save and return
        TflUser updatedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(updatedUser);
    }

    // Delete user - ADMIN only operation
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}