package org.urbanquest.userservice.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.urbanquest.userservice.dto.ApiResponse;
import org.urbanquest.userservice.dto.UserProfileUpdateDto;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.services.UserProfileService;
import org.urbanquest.userservice.services.UserService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/userprofile")
@Validated
public class UserProfileController {
    private final UserService userService;
    private final UserProfileService userProfileService;

    public UserProfileController(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileUpdateDto>> getCurrentUserProfile() {
        log.info("Received request to get current user profile");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        UUID userId = user.getId();

        UserProfileUpdateDto userProfileDto = userProfileService.getUserProfile(userId);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "User profile retrieved successfully",
                userProfileDto
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileUpdateDto>> updateCurrentUserProfile(
            @RequestBody @Valid UserProfileUpdateDto updateRequest
            ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.getUserByEmail(email);
        UUID userId = user.getId();

        log.info("Received request to update user profile: {}", user);
        UserProfileUpdateDto updatedUserProfile = userProfileService.updateUserProfile(userId, updateRequest);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "User profile update successfully",
                updatedUserProfile
        ));
    }
}
