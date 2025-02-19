package org.urbanquest.userservice.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.urbanquest.userservice.exceptions.UserAlreadyExistsException;
import org.urbanquest.userservice.exceptions.UserNotFoundException;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.models.UserProfile;
import org.urbanquest.userservice.repository.UserProfileRepository;
import org.urbanquest.userservice.repository.UserRepository;

import java.util.*;

@Service
@Slf4j
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileService userProfileService;

    public UserService(UserRepository userRepo, UserProfileService userProfileService) {
        this.userRepo = userRepo;
        this.userProfileService = userProfileService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepo.findById(id);
    }

    public User registerUser(User user) {
        logger.debug("Attempting to register new user with email: {}", user.getEmail());

        // check if user exists using email and phone
        if(userRepo.findByEmail(user.getEmail()).isPresent()) {
            logger.error("User with email {} already exists", user.getEmail());
            throw UserAlreadyExistsException.withEmail(user.getEmail());
        }

        if(userRepo.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            logger.error("Phone number {} already exists", user.getPhoneNumber());
            throw UserAlreadyExistsException.withPhoneNumber(user.getPhoneNumber());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepo.save(user);

        logger.info("User registered successfully with email {}", savedUser.getEmail());
        return savedUser;
    }

    public User updateUser(@NotNull UUID id, @Valid User user) {
        logger.debug("Attempting to update user with email: {}", user.getEmail());

        User userToUpdate = userRepo.findById(id)
                .orElseThrow(() ->  UserNotFoundException.withId(id));

        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(userToUpdate);
    }

    public User getUserByEmail(@NotNull String email) {
        logger.debug("Attempting to get user by email: {}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->  UserNotFoundException.withEmail(email));

        logger.info("User found with email: {}", user.getEmail());
        return user;
    }

//    public User getUserByEmail(@NotNull String email) {
//        logger.debug("Attempting to get user by email: {}", email);
//
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> UserNotFoundException.withEmail(email));
//
//        // Force initialization of collections
//        Hibernate.initialize(user.getBadges());
//        user.getBadges().forEach(userBadge ->
//                Hibernate.initialize(userBadge.getBadge()));
//
//        logger.info("User found with email: {} and {} badges",
//                user.getEmail(), user.getBadges().size());
//
//        return user;
//    }

    public void deleteUserById(@NotNull UUID id) {
        logger.debug("Attempting to delete user with id: {}", id);

        User user = userRepo.findById(id)
                .orElseThrow(() ->  UserNotFoundException.withId(id));

//        userProfileService.deleteUserProfileByUserId(user.getId());
        userRepo.delete(user);


        logger.info("User with id {} deleted: {}", id, user);
    }

    public void deleteUserByEmail(@NotNull String email) {
        logger.debug("Attempting to delete user with email: {}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->  UserNotFoundException.withEmail(email));

        userProfileService.deleteUserProfileByUserId(user.getId());
        userRepo.delete(user);

        logger.info("User with email {} deleted: {}", email, user);
    }

    public List<User> getAllUsers() {
        logger.info("Attempting to fetch all users");

        List<User> allUsers = userRepo.findAll();

        logger.info("All users fetched successfully");
        return allUsers;
    }
}
