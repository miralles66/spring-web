package com.miralles.spring_web.presentation.controllers;

import com.miralles.spring_web.application.ports.UserService;
import com.miralles.spring_web.domain.models.User;
import com.miralles.spring_web.presentation.dtos.UserRequestDTO;
import com.miralles.spring_web.presentation.dtos.UserResponseDTO;
import com.miralles.spring_web.presentation.dtos.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testUser", "test@example.com");
        userRequestDTO = new UserRequestDTO("testUser", "test@example.com");
    }

    @Test
    void createUser_shouldReturnCreatedResponse() {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        ResponseEntity<UserResponseDTO> response = userController.createUser(userRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getId(), response.getBody().getId());
        assertEquals(testUser.getUsername(), response.getBody().getUsername());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());
    }

    @Test
    void getUserById_shouldReturnOkResponse() {
        when(userService.getUserById(anyLong())).thenReturn(testUser);

        ResponseEntity<UserResponseDTO> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getId(), response.getBody().getId());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "user1", "user1@example.com"),
                new User(2L, "user2", "user2@example.com")
        );

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        User updatedUser = new User(1L, "updatedUser", "updated@example.com");
        UserRequestDTO updatedRequestDTO = new UserRequestDTO("updatedUser", "updated@example.com");

        when(userService.getUserById(anyLong())).thenReturn(testUser);
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(1L, updatedRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updatedUser", response.getBody().getUsername());
        assertEquals("updated@example.com", response.getBody().getEmail());
    }

    @Test
    void deleteUser_shouldReturnNoContent() {
        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        when(userService.getUserByEmail(anyString())).thenReturn(testUser);

        ResponseEntity<UserResponseDTO> response = userController.getUserByEmail("test@example.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());
    }

    @Test
    void createUser_shouldUseMapperCorrectly() {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        userController.createUser(userRequestDTO);

        // Verify that the service was called with a User object (not DTO)
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void updateUser_shouldUseMapperCorrectly() {
        UserRequestDTO updatedRequestDTO = new UserRequestDTO("updatedUser", "updated@example.com");

        when(userService.getUserById(anyLong())).thenReturn(testUser);
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(testUser);

        userController.updateUser(1L, updatedRequestDTO);

        // Verify that the service was called with User objects (not DTOs)
        verify(userService, times(1)).getUserById(anyLong());
        verify(userService, times(1)).updateUser(anyLong(), any(User.class));
    }

    @Test
    void userMapper_shouldConvertRequestDTOToUser() {
        UserRequestDTO requestDTO = new UserRequestDTO("testUser", "test@example.com");

        User user = UserMapper.toUser(requestDTO);

        assertNotNull(user);
        assertEquals(requestDTO.getUsername(), user.getUsername());
        assertEquals(requestDTO.getEmail(), user.getEmail());
        assertNull(user.getId()); // ID should be null for new users
    }

    @Test
    void userMapper_shouldConvertUserToResponseDTO() {
        User user = new User(1L, "testUser", "test@example.com");

        UserResponseDTO responseDTO = UserMapper.toUserResponseDTO(user);

        assertNotNull(responseDTO);
        assertEquals(user.getId(), responseDTO.getId());
        assertEquals(user.getUsername(), responseDTO.getUsername());
        assertEquals(user.getEmail(), responseDTO.getEmail());
    }

    @Test
    void userMapper_shouldUpdateUserFromDTO() {
        User existingUser = new User(1L, "oldUser", "old@example.com");
        UserRequestDTO requestDTO = new UserRequestDTO("newUser", "new@example.com");

        User updatedUser = UserMapper.updateUserFromDTO(requestDTO, existingUser);

        assertNotNull(updatedUser);
        assertEquals(existingUser.getId(), updatedUser.getId()); // ID should remain the same
        assertEquals(requestDTO.getUsername(), updatedUser.getUsername());
        assertEquals(requestDTO.getEmail(), updatedUser.getEmail());
    }
}