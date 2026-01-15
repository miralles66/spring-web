package com.miralles.spring_web.domain.factories;

import com.miralles.spring_web.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DefaultUserFactory.
 * These tests verify that the factory creates User objects correctly
 * and follows the Open/Closed Principle.
 */
class DefaultUserFactoryTest {

    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        userFactory = new DefaultUserFactory();
    }

    @Test
    void createUser_shouldCreateUserWithUsernameAndEmail() {
        User user = userFactory.createUser("testUser", "test@example.com");

        assertNotNull(user);
        assertNull(user.getId()); // ID should be null for new users
        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void createUser_shouldCreateUserWithIdUsernameAndEmail() {
        User user = userFactory.createUser(1L, "testUser", "test@example.com");

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void createAdminUser_shouldCreateAdminUserWithCustomCredentials() {
        User adminUser = userFactory.createAdminUser("customAdmin", "custom@example.com");

        assertNotNull(adminUser);
        assertNull(adminUser.getId());
        assertEquals("customAdmin", adminUser.getUsername());
        assertEquals("custom@example.com", adminUser.getEmail());
    }

    @Test
    void createUser_shouldCreateDifferentInstances() {
        User user1 = userFactory.createUser("user1", "user1@example.com");
        User user2 = userFactory.createUser("user2", "user2@example.com");

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotSame(user1, user2); // Should be different instances
    }

    @Test
    void createUser_shouldHandleNullId() {
        User user = userFactory.createUser(null, "testUser", "test@example.com");

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void createUser_shouldHandleEmptyUsername() {
        User user = userFactory.createUser("", "test@example.com");

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void createUser_shouldHandleEmptyEmail() {
        User user = userFactory.createUser("testUser", "");

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("", user.getEmail());
    }

    @Test
    void factory_shouldBeReusable() {
        User user1 = userFactory.createUser("user1", "user1@example.com");
        User user2 = userFactory.createUser("user2", "user2@example.com");
        User user3 = userFactory.createUser("user3", "user3@example.com");

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);

        assertEquals("user1", user1.getUsername());
        assertEquals("user2", user2.getUsername());
        assertEquals("user3", user3.getUsername());
    }

    @Test
    void factory_shouldCreateConsistentUserObjects() {
        User user1 = userFactory.createUser(1L, "testUser", "test@example.com");
        User user2 = userFactory.createUser(1L, "testUser", "test@example.com");

        assertNotNull(user1);
        assertNotNull(user2);

        // Different instances but same values
        assertNotSame(user1, user2);
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getEmail(), user2.getEmail());
    }
}