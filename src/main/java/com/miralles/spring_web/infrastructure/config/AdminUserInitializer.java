package com.miralles.spring_web.infrastructure.config;

import com.miralles.spring_web.domain.factories.UserFactory;
import com.miralles.spring_web.domain.models.User;
import com.miralles.spring_web.domain.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Initializes an admin user if one doesn't exist when the application starts.
 * This component runs after the Spring context is fully initialized.
 * 
 * This implementation follows the Open/Closed Principle by using a UserFactory
 * to create user objects, making it easy to extend user creation logic.
 */
@Component
public class AdminUserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final AdminProperties adminProperties;
    private final UserFactory userFactory;

    public AdminUserInitializer(UserRepository userRepository, AdminProperties adminProperties, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.adminProperties = adminProperties;
        this.userFactory = userFactory;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeAdminUser();
    }

    /**
     * Checks if an admin user exists, and creates one if not.
     */
    private void initializeAdminUser() {
        try {
            // Check if admin initialization is enabled
            if (!adminProperties.isEnabled()) {
                System.out.println("🔐 Admin user initialization is disabled");
                return;
            }

            String adminEmail = adminProperties.getEmail();
            String adminUsername = adminProperties.getUsername();

            Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

            if (existingAdmin.isEmpty()) {
                // Use factory to create admin user - follows Open/Closed Principle
                User adminUser = userFactory.createAdminUser(adminUsername, adminEmail);
                
                // In a real application, you would:
                // 1. Hash the password before saving
                // 2. Set additional admin-specific fields
                // 3. Assign admin role/permissions
                
                userRepository.save(adminUser);
                
                System.out.println("🔐 Admin user created: " + adminUsername + " (" + adminEmail + ")");
            } else {
                System.out.println("🔐 Admin user already exists: " + existingAdmin.get().getUsername());
            }
        } catch (Exception e) {
            System.err.println("⚠️  Failed to initialize admin user: " + e.getMessage());
            // Continue application startup even if admin initialization fails
        }
    }

    /**
     * Alternative method that could be used to create admin user with more details.
     * This shows how you might extend the functionality.
     */
    private User createAdminUserWithDetails() {
        // Use factory to create admin user with details
        User adminUser = userFactory.createAdminUser(adminProperties.getUsername(), adminProperties.getEmail());
        
        // In a real application, you might add:
        // adminUser.setRoles(List.of("ADMIN", "SUPER_USER"));
        // adminUser.setAccountStatus(AccountStatus.ACTIVE);
        // adminUser.setCreatedAt(LocalDateTime.now());
        // adminUser.setLastLogin(LocalDateTime.now());
        
        return adminUser;
    }
}