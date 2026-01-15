package com.miralles.spring_web.infrastructure.config;

import com.miralles.spring_web.domain.factories.UserFactory;
import com.miralles.spring_web.domain.models.User;
import com.miralles.spring_web.domain.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, AdminProperties adminProperties, UserFactory userFactory,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminProperties = adminProperties;
        this.userFactory = userFactory;
        this.passwordEncoder = passwordEncoder;
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
                
                // Set a default password for the admin user
                // In a real application, you would:
                // 1. Generate a secure random password
                // 2. Hash the password before saving
                // 3. Set additional admin-specific fields
                // 4. Assign admin role/permissions
                // 5. Send the password to the admin via secure email

                String defaultPassword = "admin123"; // TODO: In production, use a secure random password
                adminUser.setPassword(passwordEncoder.encode(defaultPassword));
                adminUser.setAdmin(true); // Set admin flag
                
                userRepository.save(adminUser);
                
                System.out.println("🔐 Admin user created: " + adminUsername + " (" + adminEmail + ")");
                System.out.println("⚠️  Default password set to: " + defaultPassword);
                System.out.println("⚠️  Please change this password immediately!");
            } else {
                System.out.println("🔐 Admin user already exists: " + existingAdmin.get().getUsername());
            }
        } catch (Exception e) {
            System.err.println("⚠️  Failed to initialize admin user: " + e.getMessage());
            // Continue application startup even if admin initialization fails
        }
    }
}