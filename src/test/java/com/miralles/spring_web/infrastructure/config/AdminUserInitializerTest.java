package com.miralles.spring_web.infrastructure.config;

import com.miralles.spring_web.domain.factories.UserFactory;
import com.miralles.spring_web.domain.models.User;
import com.miralles.spring_web.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationArguments applicationArguments;

    @Mock
    private AdminProperties adminProperties;

    @Mock
    private UserFactory userFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminUserInitializer adminUserInitializer;

    @BeforeEach
    void setUp() {
        // Default setup: admin initialization is enabled
        lenient().when(adminProperties.isEnabled()).thenReturn(true);
        lenient().when(adminProperties.getUsername()).thenReturn("admin");
        lenient().when(adminProperties.getEmail()).thenReturn("admin@example.com");

        // Mock user factory to return a user when createAdminUser is called
        lenient().when(userFactory.createAdminUser(anyString(), anyString())).thenAnswer(invocation -> {
            String username = invocation.getArgument(0);
            String email = invocation.getArgument(1);
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            return user;
        });
    }

    @Test
    void run_shouldCreateAdminUserWhenNotExists() throws Exception {
        // Mock the repository to return empty when checking for admin
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // Simulate ID generation
            return user;
        });

        // Execute the initializer
        adminUserInitializer.run(applicationArguments);

        // Verify that the repository was called to check for existing admin
        verify(userRepository, times(1)).findByEmail("admin@example.com");

        // Verify that a new admin user was created and saved
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void run_shouldNotCreateAdminUserWhenExists() throws Exception {
        // Mock the repository to return an existing admin user
        User existingAdmin = new User(1L, "admin", "admin@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingAdmin));

        // Execute the initializer
        adminUserInitializer.run(applicationArguments);

        // Verify that the repository was called to check for existing admin
        verify(userRepository, times(1)).findByEmail("admin@example.com");

        // Verify that no new user was created
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void run_shouldHandleRepositoryExceptionsGracefully() throws Exception {
        // Mock the repository to throw an exception
        when(userRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Database error"));

        // Execute the initializer - should not throw exception
        assertDoesNotThrow(() -> adminUserInitializer.run(applicationArguments));

        // Verify that the repository was called
        verify(userRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    void initializeAdminUser_shouldCreateUserWithCredentialsFromProperties() throws Exception {
        // Setup - configure mocks
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
        // Capture the admin properties values (whatever they are configured to be)
        String expectedUsername = adminProperties.getUsername();
        String expectedEmail = adminProperties.getEmail();
        
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            // Verify the user was created with credentials from AdminProperties
            assertEquals(expectedUsername, user.getUsername(), 
                "Username should match AdminProperties configuration");
            assertEquals(expectedEmail, user.getEmail(), 
                "Email should match AdminProperties configuration");
            user.setId(1L);
            return user;
        });

        // Execute
        adminUserInitializer.run(applicationArguments);

        // Verify
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findByEmail(expectedEmail);
    }

    @Test
    void run_shouldExecuteWithoutApplicationArguments() throws Exception {
        // Setup mocks
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // Get the expected email from AdminProperties (not hardcoded)
        String expectedEmail = adminProperties.getEmail();

        // Should work even without specific application arguments
        assertDoesNotThrow(() -> adminUserInitializer.run(applicationArguments));

        // Verify using the email from properties, not hardcoded value
        verify(userRepository, times(1)).findByEmail(expectedEmail);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void run_shouldNotCreateAdminUserWhenDisabled() {
        // Configure admin initialization to be disabled
        when(adminProperties.isEnabled()).thenReturn(false);

        // Execute the initializer
        assertDoesNotThrow(() -> adminUserInitializer.run(applicationArguments));

        // Verify that no repository operations were performed
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void run_shouldUseCustomAdminCredentials() {
        // Configure custom admin credentials
        when(adminProperties.isEnabled()).thenReturn(true);
        when(adminProperties.getUsername()).thenReturn("superadmin");
        when(adminProperties.getEmail()).thenReturn("superadmin@example.com");

        when(userRepository.findByEmail("superadmin@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        assertDoesNotThrow(() -> adminUserInitializer.run(applicationArguments));

        // Verify that custom credentials were used
        verify(userRepository, times(1)).findByEmail("superadmin@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }
}