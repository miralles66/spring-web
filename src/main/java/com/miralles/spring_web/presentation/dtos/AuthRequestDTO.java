package com.miralles.spring_web.presentation.dtos;

/**
 * Data Transfer Object for authentication requests.
 * 
 * @param email the user's email
 * @param password the user's password
 */
public record AuthRequestDTO(String email, String password) {
}