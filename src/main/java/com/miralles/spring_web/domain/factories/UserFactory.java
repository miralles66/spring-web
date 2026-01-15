package com.miralles.spring_web.domain.factories;

import com.miralles.spring_web.domain.models.User;

/**
 * Factory interface for creating User domain objects.
 * This follows the Open/Closed Principle - open for extension, closed for modification.
 */
public interface UserFactory {

    /**
     * Creates a new User with the specified username and email.
     * 
     * @param username the username for the new user
     * @param email the email for the new user
     * @return a new User instance
     */
    User createUser(String username, String email);

    /**
     * Creates a new User with the specified username, email, and ID.
     * 
     * @param id the ID for the new user
     * @param username the username for the new user
     * @param email the email for the new user
     * @return a new User instance
     */
    User createUser(Long id, String username, String email);

    /**
     * Creates a new admin User with the specified admin credentials.
     * 
     * @param username the username for the admin user
     * @param email the email for the admin user
     * @return a new admin User instance
     */
    User createAdminUser(String username, String email);
}