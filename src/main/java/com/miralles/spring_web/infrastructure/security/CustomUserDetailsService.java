package com.miralles.spring_web.infrastructure.security;

import com.miralles.spring_web.domain.models.User;
import com.miralles.spring_web.domain.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetailsService implementation that works with the User domain model.
 * This service loads user details from the repository and converts them to Spring Security's UserDetails.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username (email in this case) and returns UserDetails.
     * 
     * @param username the username (email) to load
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    /**
     * Gets the authorities (roles) for a user.
     * Uses the isAdmin flag from the user entity.
     * 
     * @param user the user to get authorities for
     * @return collection of authorities
     */
    private Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities(User user) {
        List<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<>();
        
        // Use the admin flag from the user entity
        if (user.isAdmin()) {
            authorities.add(() -> "ROLE_ADMIN");
        } else {
            authorities.add(() -> "ROLE_USER");
        }
        
        return authorities;
    }
}