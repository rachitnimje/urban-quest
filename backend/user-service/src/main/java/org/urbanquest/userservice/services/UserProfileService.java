package org.urbanquest.userservice.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.urbanquest.userservice.dto.LocationDto;
import org.urbanquest.userservice.dto.UserProfileUpdateDto;
import org.urbanquest.userservice.exceptions.UserNotFoundException;
import org.urbanquest.userservice.models.Location;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.models.UserProfile;
import org.urbanquest.userservice.repository.UserProfileRepository;
import org.urbanquest.userservice.repository.UserRepository;

import java.util.UUID;

@Service
@Validated
@Slf4j
public class UserProfileService {
    private final UserProfileRepository userProfileRepo;
    private final UserRepository userRepo;
    private final LocationService locationService;

    public UserProfileService(
            UserProfileRepository userProfileRepo,
            UserRepository userRepo,
            LocationService locationService) {
        this.userProfileRepo = userProfileRepo;
        this.userRepo = userRepo;
        this.locationService = locationService;
    }

    public void createUserProfile(User user) {
        log.info("Creating user profile: {}", user.getEmail());

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfileRepo.save(userProfile);

        log.info("User profile created: {}", userProfile);
    }

    public UserProfileUpdateDto getUserProfile(UUID id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> UserNotFoundException.withId(id));

        UserProfile userProfile = userProfileRepo.findByUser_Id(user.getId())
                .orElseThrow(() -> UserNotFoundException.withId(id));

        return UserProfileUpdateDto.fromEntities(user, userProfile);
    }

    public UserProfileUpdateDto updateUserProfile(
            @NotNull UUID userId,
            @Valid UserProfileUpdateDto updateDto) {
        log.info("Updating user profile with id: {}", userId);

        // update user details and save
        User user = userRepo.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setPhoneNumber(updateDto.getPhoneNumber());

        // update user profile details and save
        UserProfile userProfile = userProfileRepo.findByUser_Id(user.getId())
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        userProfile.setBio(updateDto.getBio());
        userProfile.setProfilePictureUrl(updateDto.getProfilePictureUrl());

        if (updateDto.getLocation() != null) {
            LocationDto locationDto = updateDto.getLocation();
            Location location = locationService.getLocationByCityStateCountry(
                    locationDto.getCity(),
                    locationDto.getState(),
                    locationDto.getCountry());
            userProfile.setLocation(location);
        }
        else {
            userProfile.setLocation(null);
        }

        User savedUser = userRepo.save(user);
        UserProfile updatedUserProfile = userProfileRepo.save(userProfile);

        UserProfileUpdateDto updatedDto = UserProfileUpdateDto.fromEntities(savedUser, updatedUserProfile);
        log.info("Updated user profile: {}", updatedDto);

        return updatedDto;
    }

    public void deleteUserProfileByUserId(@NotNull UUID userId) {
        log.info("Deleting user profile by ID: {}", userId);

        UserProfile userProfile = userProfileRepo.findByUser_Id(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        userProfileRepo.delete(userProfile);
        userProfileRepo.flush();

        log.info("Deleted user profile: {}", userProfile);
    }
}
