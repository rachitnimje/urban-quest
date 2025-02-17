package org.urbanquest.userservice.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.urbanquest.userservice.exceptions.UserAlreadyExistsException;
import org.urbanquest.userservice.exceptions.UserNotFoundException;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepo.findById(id);
    }

    public User registerUser(User user) {
        logger.debug("Attempting to register new user: {}", user);

        // check if user exists using email and phone
        if(userRepo.findByEmail(user.getEmail()).isPresent()) {
            logger.error("User with email {} already exists", user.getEmail());
            throw UserAlreadyExistsException.withEmail(user.getEmail());
        }

        if(userRepo.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            logger.error("Phone number {} already exists", user.getPhoneNumber());
            throw UserAlreadyExistsException.withPhoneNumber(user.getPhoneNumber());
        }

        // create a new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        logger.info("User registered successfully {}", savedUser);
        return savedUser;
    }

    public User updateUser(@NotNull UUID id, @Valid User user) {
        logger.debug("Attempting to update user: {}", user);

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

        logger.info("User found: {}", user);
        return user;
    }

    public void deleteUserById(@NotNull UUID id) {
        logger.debug("Attempting to delete user with id: {}", id);

        User user = userRepo.findById(id)
                .orElseThrow(() ->  UserNotFoundException.withId(id));
        userRepo.delete(user);

        logger.info("User with id {} deleted: {}", id, user);
    }

    public void deleteUserByEmail(@NotNull String email) {
        logger.debug("Attempting to delete user with email: {}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->  UserNotFoundException.withEmail(email));
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
