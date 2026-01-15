package com.miralles.spring_web.presentation.dtos;

/**
 * Data Transfer Object for authentication responses.
 * 
 * @param token the JWT token
 */
public record AuthResponseDTO(String token) {
}