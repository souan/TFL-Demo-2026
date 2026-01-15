package lu.cnfpcfullstackdev.tfl_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lu.cnfpcfullstackdev.tfl_api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Get Authorization header
        String authHeader = request.getHeader("Authorization");
        
        // 2. Check if header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token - continue without authentication
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 3. Extract token (remove "Bearer " prefix)
            String token = authHeader.substring(7);
            
            // 4. Validate token
            if (jwtService.isTokenValid(token)) {
                // 5. Extract user info from token
                Long userId = jwtService.extractUserId(token);
                String role = jwtService.extractRole(token);
                
                // 6. Create authentication object
                SimpleGrantedAuthority authority = 
                    new SimpleGrantedAuthority("ROLE_" + role);
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userId,                              // Principal (user ID)
                        null,                                // Credentials (not needed)
                        Collections.singletonList(authority) // Authorities (roles)
                    );
                
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                // 7. Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Token is invalid - log and continue without authentication
            logger.error("JWT validation failed: " + e.getMessage());
        }
        
        // 8. Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}