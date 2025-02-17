package org.urbanquest.userservice.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.urbanquest.userservice.dto.ApiResponse;
import org.urbanquest.userservice.exceptions.UserNotFoundException;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("id") @NotNull UUID id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> UserNotFoundException.withId(id));

        return ResponseEntity.ok(new ApiResponse<>(
           HttpStatus.OK.value(),
            "User retrieved successfully",
            user
        ));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable("email") @NotNull String email) {
        User user = userService.getUserByEmail(email);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "User retrieved successfully",
                user
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable("id") @NotNull UUID id,
            @RequestBody @Valid User user) {
        User updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "User updated successfully",
                updatedUser
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable("id") @NotNull UUID id) {
        userService.deleteUserById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "User deleted successfully",
                null
        ));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable("email") @NotNull String email) {
        userService.deleteUserByEmail(email);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "User deleted successfully",
                null
        ));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched all users successfully",
                users
        ));
    }
}
