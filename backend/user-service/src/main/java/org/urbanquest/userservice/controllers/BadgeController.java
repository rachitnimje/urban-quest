package org.urbanquest.userservice.controllers;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.urbanquest.userservice.dto.ApiResponse;
import org.urbanquest.userservice.models.Badge;
import org.urbanquest.userservice.services.BadgeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/badges")
@Validated
@Slf4j
public class BadgeController {

    final private BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Badge>> createBadge(@RequestBody @Validated Badge badge) {
        log.info("Received request to create badge {}", badge);

        Badge savedBadge = badgeService.createBadge(badge);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Badge deleted successfully",
                savedBadge
        ));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Badge>>> getAllBadges() {
        log.info("Received request to get all badges");

        List<Badge> badges = badgeService.getAllBadges();
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "All badges fetched successfully",
                badges
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Badge>> getBadge(@PathVariable UUID id) {
        log.info("Received request to get badge {}", id);

        Badge badge = badgeService.getBadge(id);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Badge fetched successfully",
                badge
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Badge>> updateBadge(@PathVariable @NotNull UUID id, @RequestBody Badge badge) {
        log.info("Received request to update badge {}", badge);

        Badge updatedBadge = badgeService.updateBadge(id, badge);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Badge updated successfully",
                updatedBadge
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBadge(@PathVariable @NotNull UUID id) {
        log.info("Received request to delete badge {}", id);

        badgeService.deleteBadge(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "Badge deleted successfully",
                null
        ));
    }
}
