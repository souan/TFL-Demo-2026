package lu.cnfpcfullstackdev.tfl_api.repository;

import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import lu.cnfpcfullstackdev.tfl_api.entity.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface TflUserRepository extends JpaRepository<TflUser, Long> {
    
    // Find user by username (for login)
    Optional<TflUser> findByUsername(String username);
    
    // Find user by email
    Optional<TflUser> findByEmail(String email);
    
    // Check if username exists (for registration)
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);

    List<TflUser> findByRole(UserRole role);
}
