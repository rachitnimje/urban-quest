package org.urbanquest.userservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.urbanquest.userservice.dto.ApiResponse;
import org.urbanquest.userservice.dto.AuthRequest;
import org.urbanquest.userservice.dto.AuthResponse;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.models.UserProfile;
import org.urbanquest.userservice.services.UserProfileService;
import org.urbanquest.userservice.services.UserService;
import org.urbanquest.userservice.utils.JWTUtil;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {
    final private static Logger logger = LoggerFactory.getLogger(AuthController.class);
    final private UserService userService;
    final private AuthenticationManager authenticationManager;
    final private JWTUtil jwtUtil;
    private final UserProfileService userProfileService;

    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil,
            UserProfileService userProfileService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userProfileService = userProfileService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid User user) {
        logger.info("Received request to register a new user");

        User registeredUser = userService.registerUser(user);
        userProfileService.createUserProfile(registeredUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User registered successfully",
                registeredUser
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthRequest authRequest) {
        logger.info("Received request to login user");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            logger.info("Testing");

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authRequest.getEmail());

            logger.info("User logged in successfully");
            logger.info("JWT Token: {} ", token);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "User logged in successfully",
                    new AuthResponse(token)
            ));
        }
        catch(BadCredentialsException e) {
            logger.error("User not found: {}", authRequest);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Wrong email or password",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Wrong email or password",
                    null
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        logger.info("Received request to logout user");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                SecurityContextHolder.clearContext();

                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                logger.info("User logged out successfully");
                return ResponseEntity.ok(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "User logged out successfully",
                        null
                ));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "No active session found",
                    null
            ));
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error occurred during logout",
                    null
            ));
        }
    }
}
