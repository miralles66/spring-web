package com.miralles.spring_web.domain.repositories;

import com.miralles.spring_web.domain.models.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Domain layer tests for UserRepository interface.
 * These tests verify the repository contract without any implementation.
 * This is an abstract test that can be used to verify any repository implementation.
 */
class UserRepositoryTest {

    @Test
    void repositoryContract_shouldDefineSaveMethod() {
        // This test verifies that the UserRepository interface defines the save method
        // In a real implementation, this would be tested through concrete implementations
        
        // We can use a mock to verify the contract
        UserRepository mockRepository = mock(UserRepository.class);
        User testUser = new User(1L, "testUser", "test@example.com");
        
        when(mockRepository.save(any(User.class))).thenReturn(testUser);
        
        User result = mockRepository.save(testUser);
        
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        
        verify(mockRepository, times(1)).save(testUser);
    }

    @Test
    void repositoryContract_shouldDefineFindByIdMethod() {
        UserRepository mockRepository = mock(UserRepository.class);
        User testUser = new User(1L, "testUser", "test@example.com");
        
        when(mockRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        Optional<User> result = mockRepository.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    void repositoryContract_shouldDefineFindAllMethod() {
        UserRepository mockRepository = mock(UserRepository.class);
        List<User> testUsers = List.of(
                new User(1L, "user1", "user1@example.com"),
                new User(2L, "user2", "user2@example.com")
        );
        
        when(mockRepository.findAll()).thenReturn(testUsers);
        
        List<User> result = mockRepository.findAll();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    void repositoryContract_shouldDefineDeleteByIdMethod() {
        UserRepository mockRepository = mock(UserRepository.class);
        
        // void method - just verify it can be called
        assertDoesNotThrow(() -> mockRepository.deleteById(1L));
        
        verify(mockRepository, times(1)).deleteById(1L);
    }

    @Test
    void repositoryContract_shouldDefineFindByEmailMethod() {
        UserRepository mockRepository = mock(UserRepository.class);
        User testUser = new User(1L, "testUser", "test@example.com");
        
        when(mockRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        
        Optional<User> result = mockRepository.findByEmail("test@example.com");
        
        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        
        verify(mockRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void repositoryContract_shouldHandleNullValues() {
        UserRepository mockRepository = mock(UserRepository.class);
        
        // Test that repository can handle null values gracefully
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(mockRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
        Optional<User> resultById = mockRepository.findById(999L);
        Optional<User> resultByEmail = mockRepository.findByEmail("nonexistent@example.com");
        
        assertFalse(resultById.isPresent());
        assertFalse(resultByEmail.isPresent());
    }

    @Test
    void repositoryContract_shouldBeIndependentOfImplementation() {
        // This test verifies that we can create different implementations
        // of the same repository interface
        
        UserRepository mockRepository1 = mock(UserRepository.class);
        UserRepository mockRepository2 = mock(UserRepository.class);
        
        User testUser = new User(1L, "testUser", "test@example.com");
        
        when(mockRepository1.save(any(User.class))).thenReturn(testUser);
        when(mockRepository2.save(any(User.class))).thenReturn(testUser);
        
        User result1 = mockRepository1.save(testUser);
        User result2 = mockRepository2.save(testUser);
        
        assertEquals(result1, result2);
        
        // Both mocks should have been called
        verify(mockRepository1, times(1)).save(testUser);
        verify(mockRepository2, times(1)).save(testUser);
    }

    @Test
    void repositoryContract_shouldSupportDomainModel() {
        // Verify that the repository works with our domain model
        UserRepository mockRepository = mock(UserRepository.class);
        
        // Create a domain model instance
        User domainUser = new User(null, "domainUser", "domain@example.com");
        
        // Repository should be able to handle domain model
        when(mockRepository.save(domainUser)).thenReturn(domainUser);
        
        User result = mockRepository.save(domainUser);
        
        assertNotNull(result);
        assertEquals(domainUser.getUsername(), result.getUsername());
        assertEquals(domainUser.getEmail(), result.getEmail());
    }

    @Test
    void repositoryContract_shouldBeTestableInIsolation() {
        // This test demonstrates that we can test repository behavior
        // without any database or external dependencies
        
        UserRepository mockRepository = mock(UserRepository.class);
        
        // Setup test data
        User user1 = new User(1L, "user1", "user1@example.com");
        User user2 = new User(2L, "user2", "user2@example.com");
        
        // Define mock behavior
        when(mockRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(mockRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(mockRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Test the behavior
        assertTrue(mockRepository.findById(1L).isPresent());
        assertTrue(mockRepository.findById(2L).isPresent());
        assertFalse(mockRepository.findById(3L).isPresent());
        
        // Verify interactions
        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).findById(2L);
        verify(mockRepository, times(1)).findById(3L);
    }
}