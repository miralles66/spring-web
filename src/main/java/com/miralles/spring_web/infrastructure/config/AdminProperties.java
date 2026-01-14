package com.miralles.spring_web.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for admin user initialization.
 * These properties can be configured in application.properties or application.yml.
 */
@Configuration
@ConfigurationProperties(prefix = "app.admin")
public class AdminProperties {

    /**
     * Username for the admin user
     */
    private String username = "admin";

    /**
     * Email for the admin user
     */
    private String email = "admin@example.com";

    /**
     * Whether to enable admin user initialization
     */
    private boolean enabled = true;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}