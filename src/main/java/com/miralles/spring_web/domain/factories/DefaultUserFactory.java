package com.miralles.spring_web.domain.factories;

import com.miralles.spring_web.domain.models.User;

/**
 * Default implementation of UserFactory.
 * This implementation creates standard User objects without any special
 * business logic.
 */
public class DefaultUserFactory implements UserFactory {

    @Override
    public User createUser(String username, String email) {
        return createUser(null, username, email);
    }

    @Override
    public User createUser(Long id, String username, String email) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }

    @Override
    public User createAdminUser(String username, String email) {
        // In a real application, you might add admin-specific logic here
        // such as setting admin roles, permissions, etc.
        return createUser(null, username, email);
    }
}