package com.miralles.spring_web.integration;

import com.miralles.spring_web.domain.models.User;
import com.miralles.spring_web.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserRepositoryIntegration() {
        // Create a test user
        User testUser = new User(null, "integrationTestUser", "integration@example.com");

        // Save the user
        User savedUser = userRepository.save(testUser);

        // Verify the user was saved
        assertNotNull(savedUser.getId());
        assertEquals("integrationTestUser", savedUser.getUsername());
        assertEquals("integration@example.com", savedUser.getEmail());

        // Find the user by ID
        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(savedUser.getUsername(), foundUser.getUsername());

        // Find the user by email
        User foundByEmail = userRepository.findByEmail(savedUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found by email"));

        assertEquals(savedUser.getId(), foundByEmail.getId());

        // Test findAll
        assertFalse(userRepository.findAll().isEmpty());

        // Clean up
        userRepository.deleteById(savedUser.getId());
        assertTrue(userRepository.findById(savedUser.getId()).isEmpty());
    }
}