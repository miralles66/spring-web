package com.miralles.spring_web.presentation.dtos;

import com.miralles.spring_web.domain.models.User;

public class UserMapper {

    public static User toUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        return user;
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public static User updateUserFromDTO(UserRequestDTO userRequestDTO, User existingUser) {
        existingUser.setUsername(userRequestDTO.getUsername());
        existingUser.setEmail(userRequestDTO.getEmail());
        return existingUser;
    }
}