package lu.cnfpcfullstackdev.tfl_api.service;

import lu.cnfpcfullstackdev.tfl_api.dto.request.LoginRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.request.RegisterRequestDTO;
import lu.cnfpcfullstackdev.tfl_api.dto.response.AuthResponseDTO;
import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import lu.cnfpcfullstackdev.tfl_api.entity.UserRole;
import lu.cnfpcfullstackdev.tfl_api.exception.DuplicateResourceException;
import lu.cnfpcfullstackdev.tfl_api.exception.InvalidCredentialsException;
import lu.cnfpcfullstackdev.tfl_api.repository.TflUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private TflUserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;  // We'll create this next!
    
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        // 1. Check if username already exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("User","Username",dto.getUsername());
        }
        
        // 2. Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("User","Email",dto.getEmail());
        }
        
        // 3. Create new user
        TflUser user = new TflUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));  // HASH IT!
        user.setRole(dto.getRole());
        
        // 4. Set business fields if role is BUSINESS
        if (dto.getRole() == UserRole.BUSINESS) {
            user.setBusinessName(dto.getBusinessName());
            user.setBusinessAddress(dto.getBusinessAddress());
            user.setBusinessPhone(dto.getBusinessPhone());
        }
        
        // 5. Save user to database
        TflUser savedUser = userRepository.save(user);
        
        // 6. Generate JWT token
        String token = jwtService.generateToken(savedUser);
        
        // 7. Return response with token
        return new AuthResponseDTO(
            token,
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getRole().toString()
        );
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        // 1. Find user by username
        TflUser user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException());
        
        // 2. Check password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        
        // 3. Generate JWT token
        String token = jwtService.generateToken(user);
        
        // 4. Return response
        return new AuthResponseDTO(
            token,
            user.getId(),
            user.getUsername(),
            user.getRole().toString()
        );
    }
}
