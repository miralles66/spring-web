package com.miralles.spring_web.infrastructure.persistence;

import com.miralles.spring_web.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MariaDBUserRepositoryTest {

    private MariaDBUserRepository mariaDBUserRepository;

    @BeforeEach
    void setUp() {
        mariaDBUserRepository = new MariaDBUserRepository();
        // Reset the ID generator for each test
        ReflectionTestUtils.setField(mariaDBUserRepository, "idGenerator",
                new java.util.concurrent.atomic.AtomicLong(1));
    }

    @Test
    void save_shouldCreateNewUserWithGeneratedId() {
        User newUser = new User(null, "mariaUser", "maria@example.com");

        User savedUser = mariaDBUserRepository.save(newUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(1L, savedUser.getId());
        assertEquals("mariaUser", savedUser.getUsername());
        assertEquals("maria@example.com", savedUser.getEmail());
    }

    @Test
    void save_shouldUpdateExistingUser() {
        User existingUser = new User(1L, "oldUser", "old@example.com");
        mariaDBUserRepository.save(existingUser);

        User updatedUser = new User(1L, "updatedUser", "updated@example.com");
        User result = mariaDBUserRepository.save(updatedUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        User user = new User(null, "testUser", "test@example.com");
        User savedUser = mariaDBUserRepository.save(user);

        Optional<User> foundUser = mariaDBUserRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getUsername(), foundUser.get().getUsername());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<User> foundUser = mariaDBUserRepository.findById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        mariaDBUserRepository.save(new User(null, "user1", "user1@example.com"));
        mariaDBUserRepository.save(new User(null, "user2", "user2@example.com"));

        List<User> allUsers = mariaDBUserRepository.findAll();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoUsers() {
        List<User> allUsers = mariaDBUserRepository.findAll();

        assertNotNull(allUsers);
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void deleteById_shouldRemoveUser() {
        User user = mariaDBUserRepository.save(new User(null, "testUser", "test@example.com"));
        Long userId = user.getId();

        mariaDBUserRepository.deleteById(userId);

        Optional<User> deletedUser = mariaDBUserRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void deleteById_shouldNotFailWhenUserDoesNotExist() {
        assertDoesNotThrow(() -> mariaDBUserRepository.deleteById(999L));
    }

    @Test
    void findByEmail_shouldReturnUserWhenExists() {
        User user = mariaDBUserRepository.save(new User(null, "testUser", "test@example.com"));

        Optional<User> foundUser = mariaDBUserRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void findByEmail_shouldReturnEmptyWhenNotExists() {
        Optional<User> foundUser = mariaDBUserRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void findByEmail_shouldBeCaseSensitive() {
        mariaDBUserRepository.save(new User(null, "testUser", "test@example.com"));

        Optional<User> foundUser = mariaDBUserRepository.findByEmail("TEST@EXAMPLE.COM");

        assertFalse(foundUser.isPresent(), "Email search should be case sensitive");
    }

    @Test
    void multipleUsers_shouldHaveUniqueIds() {
        User user1 = mariaDBUserRepository.save(new User(null, "user1", "user1@example.com"));
        User user2 = mariaDBUserRepository.save(new User(null, "user2", "user2@example.com"));
        User user3 = mariaDBUserRepository.save(new User(null, "user3", "user3@example.com"));

        assertNotEquals(user1.getId(), user2.getId());
        assertNotEquals(user1.getId(), user3.getId());
        assertNotEquals(user2.getId(), user3.getId());
    }

    @Test
    void repository_shouldBeThreadSafe() throws InterruptedException {
        // This test verifies that the ConcurrentHashMap implementation is thread-safe
        Runnable saveUserTask = () -> {
            for (int i = 0; i < 100; i++) {
                mariaDBUserRepository.save(new User(null, "user" + i, "user" + i + "@example.com"));
            }
        };

        Thread thread1 = new Thread(saveUserTask);
        Thread thread2 = new Thread(saveUserTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // Should have 200 users (100 from each thread)
        List<User> allUsers = mariaDBUserRepository.findAll();
        assertEquals(200, allUsers.size());
    }
}