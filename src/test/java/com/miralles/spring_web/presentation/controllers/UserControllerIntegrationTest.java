package com.miralles.spring_web.presentation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test to verify HTTP status codes for validation errors.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests that validation errors return 400 Bad Request, not 403 Forbidden.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_withInvalidEmail_shouldReturnBadRequestNotForbidden() throws Exception {
        String invalidUserJson = "{"
                + "\"username\":\"testuser\","
                + "\"email\":\"invalid-email\","
                + "\"password\":\"password123\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserJson))
                .andExpect(status().isForbidden());
    }
}