package lu.cnfpcfullstackdev.tfl_api.service;

import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import lu.cnfpcfullstackdev.tfl_api.entity.UserRole;
import lu.cnfpcfullstackdev.tfl_api.repository.TflUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TflUserService {

    @Autowired
    private TflUserRepository userRepository;

    // Get all users
    public List<TflUser> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public TflUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Get all businesses (useful for dropdown menus)
    public List<TflUser> getAllBusinesses() {
        return userRepository.findByRole(UserRole.BUSINESS);
    }

    // Create user (simplified - no password hashing yet, that's Day 3!)
    public TflUser createUser(TflUser user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        return userRepository.save(user);
    }

    // Update user
    public TflUser updateUser(Long id, TflUser updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setRole(updatedUser.getRole());
                    user.setBusinessName(updatedUser.getBusinessName());
                    user.setBusinessAddress(updatedUser.getBusinessAddress());
                    user.setBusinessPhone(updatedUser.getBusinessPhone());
                    // Note: Password update will be handled differently in Day 3
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Delete user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}