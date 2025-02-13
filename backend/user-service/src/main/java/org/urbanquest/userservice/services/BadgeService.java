package org.urbanquest.userservice.services;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.urbanquest.userservice.exceptions.BadgeAlreadyExistsException;
import org.urbanquest.userservice.exceptions.BadgeNotFoundException;
import org.urbanquest.userservice.models.Badge;
import org.urbanquest.userservice.repository.BadgeRepository;

import java.util.List;
import java.util.UUID;

@Service
@Validated
public class BadgeService {
    final static private Logger logger = LoggerFactory.getLogger(BadgeService.class);
    final private BadgeRepository badgeRepository;

    public BadgeService(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    public List<Badge> getAllBadges() {
        logger.info("Fetching all badges");
        return badgeRepository.findAll();
    }

    public Badge createBadge(Badge badge) {
        logger.info("Creating badge {}", badge);

        if(badgeRepository.findByName(badge.getName()).isPresent()) {
            logger.error("Badge already exists {}", badge.getName());
            throw BadgeAlreadyExistsException.withBadge(badge);
        }

        Badge savedBadge = badgeRepository.save(badge);
        logger.info("Successfully created badge {}", savedBadge);
        return savedBadge;
    }

    public Badge updateBadge(UUID id, Badge badge) {
        logger.debug("Updating badge {}", badge);

        Badge existingBadge = badgeRepository.findById(id)
                .orElseThrow(() -> BadgeNotFoundException.withId(id));

        existingBadge.setName(badge.getName());
        existingBadge.setDescription(badge.getDescription());

        Badge updatedBadge = badgeRepository.save(existingBadge);
        logger.info("Badge updated successfully {}", updatedBadge);
        return updatedBadge;
    }

    public void deleteBadge(@NotNull UUID id) {
        logger.debug("Deleting badge {}", id);

        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> BadgeNotFoundException.withId(id));

        badgeRepository.delete(badge);
        logger.info("Badge deleted successfully {}", id);
    }

    public Badge getBadge(UUID id) {
        logger.debug("Fetching badge with id {}", id);
        return badgeRepository.findById(id)
                .orElseThrow(() -> BadgeNotFoundException.withId(id));
    }
}
