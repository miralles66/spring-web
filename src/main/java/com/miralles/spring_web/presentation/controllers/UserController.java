package com.miralles.spring_web.presentation.controllers;

import com.miralles.spring_web.application.ports.UserService;
import com.miralles.spring_web.presentation.dtos.UserRequestDTO;
import com.miralles.spring_web.presentation.dtos.UserResponseDTO;
import com.miralles.spring_web.presentation.dtos.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        var user = UserMapper.toUser(userRequestDTO);
        var createdUser = userService.createUser(user);
        var responseDTO = UserMapper.toUserResponseDTO(createdUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        var user = userService.getUserById(id);
        var responseDTO = UserMapper.toUserResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        var users = userService.getAllUsers();
        var responseDTOs = users.stream()
                .map(UserMapper::toUserResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        var existingUser = userService.getUserById(id);
        var updatedUser = UserMapper.updateUserFromDTO(userRequestDTO, existingUser);
        var result = userService.updateUser(id, updatedUser);
        var responseDTO = UserMapper.toUserResponseDTO(result);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        var user = userService.getUserByEmail(email);
        var responseDTO = UserMapper.toUserResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }
}