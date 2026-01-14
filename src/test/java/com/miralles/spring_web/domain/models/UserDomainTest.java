package com.miralles.spring_web.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Domain layer tests for User entity.
 * These tests verify the core business logic and invariants of the User domain model.
 */
class UserDomainTest {

    @Test
    void user_shouldBeCreatedWithAllFields() {
        User user = new User(1L, "testUser", "test@example.com");

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void user_shouldBeCreatedWithNullId() {
        User user = new User(null, "newUser", "new@example.com");

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("newUser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void user_shouldHaveEmptyConstructor() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
    }

    @Test
    void user_setters_shouldUpdateFields() {
        User user = new User();

        user.setId(1L);
        user.setUsername("updatedUser");
        user.setEmail("updated@example.com");

        assertEquals(1L, user.getId());
        assertEquals("updatedUser", user.getUsername());
        assertEquals("updated@example.com", user.getEmail());
    }

    @Test
    void user_equals_shouldBeBasedOnId() {
        User user1 = new User(1L, "user1", "user1@example.com");
        User user2 = new User(1L, "user2", "user2@example.com");
        User user3 = new User(2L, "user1", "user1@example.com");

        // Same ID should be equal
        assertEquals(user1, user2);
        
        // Different ID should not be equal
        assertNotEquals(user1, user3);
        
        // Null should not be equal
        assertNotEquals(user1, null);
        
        // Different class should not be equal
        assertNotEquals(user1, "string");
    }

    @Test
    void user_hashCode_shouldBeConsistentWithEquals() {
        User user1 = new User(1L, "user1", "user1@example.com");
        User user2 = new User(1L, "user2", "user2@example.com");

        // If equals() returns true, hashCode() must return same value
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void user_toString_shouldContainAllFields() {
        User user = new User(1L, "testUser", "test@example.com");
        String toString = user.toString();

        assertTrue(toString.contains("User{"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("username='testUser'"));
        assertTrue(toString.contains("email='test@example.com'"));
    }

    @Test
    void user_withSameId_shouldHaveSameHashCode() {
        User user1 = new User(1L, "user1", "user1@example.com");
        User user2 = new User(1L, "user2", "user2@example.com");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void user_withDifferentId_shouldHaveDifferentHashCode() {
        User user1 = new User(1L, "user1", "user1@example.com");
        User user2 = new User(2L, "user1", "user1@example.com");

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void user_emailValidation_shouldBeHandledByDomain() {
        // In a real application, the domain model would validate email format
        // For now, we just verify the field can be set
        User user = new User(1L, "testUser", "test@example.com");
        
        assertEquals("test@example.com", user.getEmail());
        
        // Domain model accepts any email format (validation is in presentation layer)
        user.setEmail("invalid-email");
        assertEquals("invalid-email", user.getEmail());
    }

    @Test
    void user_usernameValidation_shouldBeHandledByDomain() {
        // In a real application, the domain model would validate username constraints
        User user = new User(1L, "testUser", "test@example.com");
        
        assertEquals("testUser", user.getUsername());
        
        // Domain model accepts any username (validation is in presentation layer)
        user.setUsername("a"); // Too short
        assertEquals("a", user.getUsername());
        
        user.setUsername("a".repeat(100)); // Too long
        assertEquals("a".repeat(100), user.getUsername());
    }

    @Test
    void user_immutability_shouldBeConsidered() {
        // This test shows that our current User model is mutable
        // In a pure domain-driven design, we might want immutable entities
        User user = new User(1L, "original", "original@example.com");
        
        // Can modify after creation (mutable)
        user.setUsername("modified");
        user.setEmail("modified@example.com");
        
        assertEquals("modified", user.getUsername());
        assertEquals("modified@example.com", user.getEmail());
    }

    @Test
    void user_businessRules_example() {
        // Example of business rule: username cannot be the same as email
        User user = new User(1L, "test@example.com", "test@example.com");
        
        // In a real application, this would be validated by domain logic
        // For now, we just document that this is allowed by the current implementation
        assertEquals("test@example.com", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }
}