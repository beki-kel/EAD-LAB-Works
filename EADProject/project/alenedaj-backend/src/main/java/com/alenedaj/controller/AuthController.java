package com.alenedaj.controller;

import com.alenedaj.dto.LoginDTO;
import com.alenedaj.dto.UserDTO;
import com.alenedaj.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Send email verification code
    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestParam String email) throws MessagingException {
        logger.info("Sending verification to {}", email);
        authService.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent to " + email);
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserDTO userDTO) {
        logger.info("Registering user with email {}", userDTO.getEmail());
        String token = authService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully", "token", token));
    }

    // Login user (Store JWT in cookie & return role)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        logger.info("User {} attempting login", loginDTO.getEmail());
        String token = authService.login(loginDTO);
        String role = authService.getUserRole(loginDTO.getEmail());

        // Store token in HTTP-only cookie
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(Map.of("message", "Login successful", "role", role));
    }

    // Logout (Clear JWT Cookie)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Expire immediately
        response.addCookie(jwtCookie);
        logger.info("User logged out successfully");
        return ResponseEntity.ok("Logout successful");
    }

    // Update User Location (Auto-Fill Coordinates)
    @PutMapping("/update-location/{userId}")
    public ResponseEntity<String> updateUserLocation(@PathVariable String userId, @RequestBody String location) {
        logger.info("Updating location for userId {}: {}", userId, location);
        authService.updateUserLocation(userId, location);
        return ResponseEntity.ok("User location updated successfully.");
    }

    // Get User Info (Email & Role)
    @GetMapping("/user-info")
    public ResponseEntity<Map<String, String>> getUserInfo(@RequestParam String email) {
        String role = authService.getUserRole(email);
        return ResponseEntity.ok(Map.of("email", email, "role", role));
    }
}
