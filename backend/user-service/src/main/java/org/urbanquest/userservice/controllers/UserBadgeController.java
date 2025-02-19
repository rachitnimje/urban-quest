package org.urbanquest.userservice.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.urbanquest.userservice.dto.ApiResponse;
import org.urbanquest.userservice.dto.BadgeAwardRequest;
import org.urbanquest.userservice.dto.UserBadgeResponse;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.models.UserBadge;
import org.urbanquest.userservice.services.UserBadgeService;
import org.urbanquest.userservice.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/badges")
@Validated
@Slf4j
public class UserBadgeController {
    private final UserBadgeService userBadgeService;
    private final UserService userService;

    public UserBadgeController(UserBadgeService userBadgeService, UserService userService) {
        this.userBadgeService = userBadgeService;
        this.userService = userService;
    }

    @PostMapping("/award")
    public ResponseEntity<ApiResponse<UserBadge>> awardBadge(@RequestBody @Valid BadgeAwardRequest awardRequest) {
        log.info("Received request to award badge: {}", awardRequest);

        UserBadge userBadge = userBadgeService.awardBadge(awardRequest.getUserId(), awardRequest.getBadgeId());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Badge awarded successfully",
                userBadge
        ));
    }

    /*
        Retrieves all the badges earned by the current user
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<UserBadgeResponse>>> getBadge() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserBadgeResponse> userBadges = userBadgeService.getAllBadgesOfCurrentUser(email);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "All badges earned by current user fetched successfully",
                userBadges
        ));
    }
}
