package com.miralles.spring_web.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miralles.spring_web.infrastructure.security.JwtUtil;
import com.miralles.spring_web.presentation.dtos.AuthRequestDTO;
import com.miralles.spring_web.presentation.dtos.AuthResponseDTO;

/**
 * Authentication controller for handling login and token generation.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Authenticates a user and returns a JWT token.
     * 
     * @param authRequest the authentication request containing email and password
     * @return ResponseEntity containing the JWT token
     * @throws Exception if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO authRequest) throws Exception {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.email(),
                            authRequest.password()));

            // Generate JWT token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.email());
            final String token = jwtUtil.generateToken(userDetails);

            // Return the token
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            throw new Exception("Invalid credentials", e);
        }
    }

    /**
     * Health check endpoint for authentication service.
     * 
     * @return simple health check response
     */
    @PostMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Auth service is healthy");
    }
}