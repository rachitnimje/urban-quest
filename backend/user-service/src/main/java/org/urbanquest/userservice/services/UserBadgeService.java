package org.urbanquest.userservice.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.urbanquest.userservice.dto.UserBadgeResponse;
import org.urbanquest.userservice.exceptions.BadgeAlreadyAwardedException;
import org.urbanquest.userservice.exceptions.BadgeNotFoundException;
import org.urbanquest.userservice.exceptions.UserNotFoundException;
import org.urbanquest.userservice.models.Badge;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.models.UserBadge;
import org.urbanquest.userservice.repository.BadgeRepository;
import org.urbanquest.userservice.repository.UserBadgeRepository;
import org.urbanquest.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserBadgeService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public UserBadgeService(
            UserRepository userRepository,
            BadgeRepository badgeRepository,
            UserBadgeRepository userBadgeRepository) {
        this.userRepository = userRepository;
        this.badgeRepository = badgeRepository;
        this.userBadgeRepository = userBadgeRepository;
    }

    @Transactional
    public UserBadge awardBadge(UUID userId, UUID badgeId) {
        log.info("Awarding Badge {} to user {}", badgeId, userId);

        if(userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            log.info("User already awarded badge {}", badgeId);
            throw BadgeAlreadyAwardedException.withId(userId, badgeId);
        }

        // fetch user and badge from their entities
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> BadgeNotFoundException.withId(badgeId));

        // create new badge based on userId and badgeId
        UserBadge userBadge = new UserBadge();
        userBadge.setUser(user);
        userBadge.setBadge(badge);

        // save into UserBadge
        UserBadge savedUserBadge = userBadgeRepository.save(userBadge);

        log.info("Badge {} awarded to user {}", badgeId, userId);
        log.info(String.valueOf(userBadge));

        return savedUserBadge;
    }

    public List<UserBadgeResponse> getAllBadgesOfCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.withEmail(email));

        List<UserBadge> userBadges = userBadgeRepository.findByUser_Id(user.getId());

        return userBadges.stream()
                .map(userBadge -> new UserBadgeResponse(
                        userBadge.getId(),
                        userBadge.getBadge().getId(),
                        userBadge.getBadge().getName(),
                        userBadge.getBadge().getDescription(),
                        userBadge.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
