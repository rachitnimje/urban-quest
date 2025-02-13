package org.urbanquest.userservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "city cannot be blank")
    private String city;

    @NotBlank(message = "state cannot be blank")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "country cannot be blank")
    @Column(nullable = false)
    private String country;

    @NotNull(message = "latitude cannot be blank")
    @Column(nullable = false)
    private double latitude;

    @NotNull(message = "longitude cannot be blank")
    @Column(nullable = false)
    private double longitude;

    @Column(name = "google_maps_url")
    private String googleMapsUrl;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
