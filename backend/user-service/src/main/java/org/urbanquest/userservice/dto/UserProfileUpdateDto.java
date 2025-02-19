package org.urbanquest.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.models.UserProfile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDto {
    @NotBlank(message = "first name cannot be empty")
    private String firstName;
    private String lastName;

    @NotBlank(message = "phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "email cannot be empty")
    @Email
    private String email;

    private String bio;
    private String profilePictureUrl;
//    private Location location;
    private LocationDto location;

    public static UserProfileUpdateDto fromEntities(User user, UserProfile profile) {
        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());

        dto.setBio(profile.getBio());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());

        if (profile.getLocation() != null) {
            LocationDto locationDto = new LocationDto();

            locationDto.setCity(profile.getLocation().getCity());
            locationDto.setState(profile.getLocation().getState());
            locationDto.setCountry(profile.getLocation().getCountry());
            dto.setLocation(locationDto);

            dto.setLocation(locationDto);
        }
        return dto;
    }
}
