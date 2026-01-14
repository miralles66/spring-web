package com.miralles.spring_web.presentation.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDTOValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validUserRequestDTO_shouldHaveNoViolations() {
        UserRequestDTO validDTO = new UserRequestDTO("validUser", "valid@example.com");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(validDTO);

        assertTrue(violations.isEmpty(), "Valid DTO should have no constraint violations");
    }

    @Test
    void emptyUsername_shouldHaveViolation() {
        UserRequestDTO invalidDTO = new UserRequestDTO("", "test@example.com");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty(), "Empty username should cause validation violation");
        // Empty string violates both @NotBlank and @Size constraints
        assertEquals(2, violations.size());
        
        // Check that at least one violation is for username
        boolean hasUsernameViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username"));
        assertTrue(hasUsernameViolation, "Should have username violation");
    }

    @Test
    void usernameTooShort_shouldHaveViolation() {
        UserRequestDTO invalidDTO = new UserRequestDTO("ab", "test@example.com");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty(), "Username too short should cause validation violation");
        assertEquals(1, violations.size());
        
        ConstraintViolation<UserRequestDTO> violation = violations.iterator().next();
        assertEquals("username", violation.getPropertyPath().toString());
        assertTrue(violation.getMessage().contains("between 3 and 50"));
    }

    @Test
    void usernameTooLong_shouldHaveViolation() {
        String longUsername = "a".repeat(51); // 51 characters
        UserRequestDTO invalidDTO = new UserRequestDTO(longUsername, "test@example.com");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty(), "Username too long should cause validation violation");
        assertEquals(1, violations.size());
        
        ConstraintViolation<UserRequestDTO> violation = violations.iterator().next();
        assertEquals("username", violation.getPropertyPath().toString());
        assertTrue(violation.getMessage().contains("between 3 and 50"));
    }

    @Test
    void emptyEmail_shouldHaveViolation() {
        UserRequestDTO invalidDTO = new UserRequestDTO("validUser", "");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty(), "Empty email should cause validation violation");
        assertEquals(1, violations.size());
        
        ConstraintViolation<UserRequestDTO> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertTrue(violation.getMessage().contains("required"));
    }

    @Test
    void invalidEmailFormat_shouldHaveViolation() {
        UserRequestDTO invalidDTO = new UserRequestDTO("validUser", "invalid-email");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty(), "Invalid email format should cause validation violation");
        assertEquals(1, violations.size());
        
        ConstraintViolation<UserRequestDTO> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertTrue(violation.getMessage().contains("valid"));
    }

    @Test
    void multipleViolations_shouldBeReported() {
        UserRequestDTO invalidDTO = new UserRequestDTO("", "invalid-email");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty(), "Multiple violations should be reported");
        // Empty username causes 2 violations, invalid email causes 1 = total 3
        assertEquals(3, violations.size(), "Should have 3 violations: 2 for username, 1 for email");
        
        // Verify we have violations for both fields
        long usernameViolations = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("username"))
                .count();
        long emailViolations = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("email"))
                .count();
        
        assertEquals(2, usernameViolations, "Should have 2 username violations");
        assertEquals(1, emailViolations, "Should have 1 email violation");
    }

    @Test
    void validEmailFormats_shouldPassValidation() {
        String[] validEmails = {
                "test@example.com",
                "first.last@sub.domain.com",
                "user+tag@example.org",
                "user_name@example.co.uk"
        };

        for (String email : validEmails) {
            UserRequestDTO validDTO = new UserRequestDTO("validUser", email);
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(validDTO);
            assertTrue(violations.isEmpty(), "Valid email " + email + " should pass validation");
        }
    }

    @Test
    void boundaryUsernameLengths_shouldPassValidation() {
        // Test minimum length (3 characters)
        UserRequestDTO minLengthDTO = new UserRequestDTO("abc", "test@example.com");
        assertTrue(validator.validate(minLengthDTO).isEmpty());

        // Test maximum length (50 characters)
        String maxLengthUsername = "a".repeat(50);
        UserRequestDTO maxLengthDTO = new UserRequestDTO(maxLengthUsername, "test@example.com");
        assertTrue(validator.validate(maxLengthDTO).isEmpty());
    }
}