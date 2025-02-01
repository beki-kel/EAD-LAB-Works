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
import org.springframework.stereotype.Controller;  // Using @Controller for view resolution
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }


    @PostMapping("/api/send-verification")
    public ResponseEntity<Map<String, String>> sendVerification(@RequestParam String email) throws MessagingException {
        try {
            logger.info("Sending verification to {}", email);
            authService.sendVerificationCode(email);
            return ResponseEntity.ok(Map.of("message", "Verification code sent to " + email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            logger.info("Registering user with email {}", userDTO.getEmail());
            String token = authService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User registered successfully", "token", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            logger.info("User {} attempting login", loginDTO.getEmail());
            String token = authService.login(loginDTO);
            String role = authService.getUserRole(loginDTO.getEmail());

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(Map.of("message", "Login successful", "role", role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        logger.info("User logged out successfully");
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @PutMapping("/api/update-location/{userId}")
    public ResponseEntity<Map<String, String>> updateUserLocation(@PathVariable String userId, @RequestBody String location) {
        try {
            logger.info("Updating location for userId {}: {}", userId, location);
            authService.updateUserLocation(userId, location);
            return ResponseEntity.ok(Map.of("message", "User location updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/user-info")
    public ResponseEntity<Map<String, String>> getUserInfo(@RequestParam String email) {
        try {
            String role = authService.getUserRole(email);
            return ResponseEntity.ok(Map.of("email", email, "role", role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
