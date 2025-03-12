package org.urbanquest.userservice.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.urbanquest.userservice.dto.ApiResponse;
import org.urbanquest.userservice.dto.VisitedLocationRequestDto;
import org.urbanquest.userservice.dto.VisitedLocationResponseDto;
import org.urbanquest.userservice.models.VisitedLocation;
import org.urbanquest.userservice.services.VisitedLocationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/locations/visited")
public class VisitedLocationController {
    private final VisitedLocationService visitedLocationService;

    public VisitedLocationController(VisitedLocationService visitedLocationService) {
        this.visitedLocationService = visitedLocationService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<VisitedLocation>> createVisitedLocation(
            @RequestBody @Valid VisitedLocationRequestDto visitedLocationRequestDto) {
        VisitedLocation savedVisitedLocation = visitedLocationService.addVisitedLocation(
                visitedLocationRequestDto.getUserId(),
                visitedLocationRequestDto.getLocationId());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Visited location added successfully",
                savedVisitedLocation
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<VisitedLocationResponseDto>>> getVisitedLocations(
            @PathVariable @NotNull UUID userId) {
        List<VisitedLocationResponseDto> visitedLocations = visitedLocationService.getVisitedLocations(userId);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched visited locations successfully",
                visitedLocations
        ));
    }
}
