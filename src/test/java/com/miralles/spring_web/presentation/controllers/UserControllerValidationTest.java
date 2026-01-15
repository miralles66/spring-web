package com.miralles.spring_web.presentation.controllers;

import com.miralles.spring_web.presentation.dtos.UserRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserRequestDTO validation.
 * These tests verify that validation annotations work correctly
 * without requiring a full Spring context.
 */
class UserControllerValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests that creating a user with invalid email fails validation.
     */
    @Test
    void createUser_withInvalidEmail_shouldFailValidation() {
        UserRequestDTO invalidUser = new UserRequestDTO(
                "testuser",
                "invalid-email",  // Invalid email format
                "password123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidUser);

        assertFalse(violations.isEmpty(), "Validation should fail for invalid email");
        
        boolean hasEmailViolation = violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));
        
        assertTrue(hasEmailViolation, "Should have email validation violation");
    }

    /**
     * Tests that creating a user with missing username fails validation.
     */
    @Test
    void createUser_withMissingUsername_shouldFailValidation() {
        UserRequestDTO invalidUser = new UserRequestDTO();
        invalidUser.setEmail("test@example.com");
        invalidUser.setPassword("password123");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidUser);

        assertFalse(violations.isEmpty(), "Validation should fail for missing username");
        
        boolean hasUsernameViolation = violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("username"));
        
        assertTrue(hasUsernameViolation, "Should have username validation violation");
    }

    /**
     * Tests that creating a user with missing email fails validation.
     */
    @Test
    void createUser_withMissingEmail_shouldFailValidation() {
        UserRequestDTO invalidUser = new UserRequestDTO();
        invalidUser.setUsername("testuser");
        invalidUser.setPassword("password123");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidUser);

        assertFalse(violations.isEmpty(), "Validation should fail for missing email");
        
        boolean hasEmailViolation = violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));
        
        assertTrue(hasEmailViolation, "Should have email validation violation");
    }

    /**
     * Tests that creating a user with short password fails validation.
     */
    @Test
    void createUser_withShortPassword_shouldFailValidation() {
        UserRequestDTO invalidUser = new UserRequestDTO(
                "testuser",
                "test@example.com",
                "short"  // Too short
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidUser);

        assertFalse(violations.isEmpty(), "Validation should fail for short password");
        
        boolean hasPasswordViolation = violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password"));
        
        assertTrue(hasPasswordViolation, "Should have password validation violation");
    }

    /**
     * Tests that creating a user with valid data passes validation.
     */
    @Test
    void createUser_withValidData_shouldPassValidation() {
        UserRequestDTO validUser = new UserRequestDTO(
                "testuser",
                "test@example.com",
                "password123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(validUser);

        assertTrue(violations.isEmpty(), "Validation should pass for valid user data");
    }
}