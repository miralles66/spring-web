package com.miralles.spring_web.infrastructure.config;

import com.miralles.spring_web.domain.factories.DefaultUserFactory;
import com.miralles.spring_web.domain.factories.UserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for UserFactory.
 * This configuration provides the default UserFactory implementation
 * and allows for easy replacement with custom implementations.
 */
@Configuration
public class UserFactoryConfig {

    /**
     * Creates a bean for the default UserFactory implementation.
     * 
     * @return a new DefaultUserFactory instance
     */
    @Bean
    public UserFactory userFactory() {
        return new DefaultUserFactory();
    }
}