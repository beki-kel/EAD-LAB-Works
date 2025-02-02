package com.alenedaj.service;

import com.alenedaj.dto.LoginDTO;
import com.alenedaj.dto.UserDTO;
import com.alenedaj.model.User;
import com.alenedaj.model.VerificationCode;
import com.alenedaj.repository.UserRepository;
import com.alenedaj.repository.VerificationCodeRepository;
import com.alenedaj.utils.EmailUtil;
import com.alenedaj.utils.JwtUtil;
import jakarta.mail.MessagingException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.alenedaj.utils.MapboxUtil;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private MapboxUtil mapboxUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Sends a verification code to the provided email.
     * Checks if the email is already registered.
     */
    public void sendVerificationCode(String email) throws MessagingException {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Check if a valid verification code already exists
        verificationCodeRepository.findByEmail(email).ifPresent(existingCode -> {
            if (LocalDateTime.now().isAfter(existingCode.getExpiresAt())) {
                // Delete expired code
                verificationCodeRepository.delete(existingCode);
            } else {
                throw new RuntimeException("A verification code was already sent. Please check your email.");
            }
        });

        // Generate a 6-digit random code
        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        // Save new verification code with a 5-minute expiration
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        verificationCodeRepository.save(verificationCode);
        emailUtil.sendEmail(email, "Email Verification Code", code);
    }

    /**
     * Registers a new user.
     * Checks if the user already exists and validates the verification code.
     */
    public String register(UserDTO userDTO) {
        String email = userDTO.getEmail();

        // Check if user with this email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Retrieve and validate the verification code
        VerificationCode storedCode = verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Verification code not requested"));

        if (LocalDateTime.now().isAfter(storedCode.getExpiresAt())) {
            verificationCodeRepository.delete(storedCode);
            throw new RuntimeException("Verification code expired. Request a new one.");
        }

        if (!storedCode.getCode().equals(userDTO.getVerificationCode())) {
            throw new RuntimeException("Invalid verification code");
        }

        // Set role based on email (for example, a specific email is ADMIN)
        Set<String> roles = email.equalsIgnoreCase("bereket.kelay@aait.edu.et") ? Set.of("ADMIN") : Set.of("USER");

        // Save the new user with an encoded password and roles
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);
        // Delete the used verification code
        verificationCodeRepository.delete(storedCode);

        // Generate and return a JWT token with the user's role
        return jwtUtil.generateToken(email, roles);
    }

    /**
     * Authenticates a user and returns a JWT token.
     */
    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRoles());
    }

    /**
     * Retrieves the role of the user with the given email.
     */
    public String getUserRole(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRoles().iterator().next();
    }

    public java.util.Map<String, String> getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Build a map with user details. Adjust keys as desired.
        return java.util.Map.of(
                "id", user.getId().toHexString(),  // assuming the id is an ObjectId
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRoles().iterator().next()
        );
    }

    /**
     * Updates the location and coordinates for the specified user.
     */
    public void updateUserLocation(String userId, String newLocation) {
        User user = userRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        double[] coordinates = mapboxUtil.getCoordinates(newLocation);
        user.setLocation(newLocation);
        user.setLatitude(coordinates[0]);
        user.setLongitude(coordinates[1]);

        userRepository.save(user);
    }
}
