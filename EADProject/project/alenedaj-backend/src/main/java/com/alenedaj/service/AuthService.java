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
import java.util.HashSet;
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


    public void sendVerificationCode(String email) throws MessagingException {
        verificationCodeRepository.findByEmail(email).ifPresent(existingCode -> {
            // ✅ Check if the code is expired
            if (LocalDateTime.now().isAfter(existingCode.getExpiresAt())) {
                verificationCodeRepository.delete(existingCode); // ❌ Delete expired code
            } else {
                throw new RuntimeException("A verification code was already sent. Please check your email.");
            }
        });

        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit code

        // ✅ Save new verification code
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        verificationCodeRepository.save(verificationCode);
        emailUtil.sendEmail(email, "Email Verification Code", code);
    }


    public String register(UserDTO userDTO) {
        String email = userDTO.getEmail();

        VerificationCode storedCode = verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Verification code not requested"));

        if (LocalDateTime.now().isAfter(storedCode.getExpiresAt())) {
            verificationCodeRepository.delete(storedCode);
            throw new RuntimeException("Verification code expired. Request a new one.");
        }

        if (!storedCode.getCode().equals(userDTO.getVerificationCode())) {
            throw new RuntimeException("Invalid verification code");
        }

        // ✅ Set Role Based on Email
        Set<String> roles = email.equalsIgnoreCase("bereket.kelay@aait.edu.et") ? Set.of("ADMIN") : Set.of("USER");

        // ✅ Save User with Role
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);
        verificationCodeRepository.delete(storedCode);

        // ✅ Generate Token with Role
        return jwtUtil.generateToken(email, roles);
    }

    // Login user & return JWT with role
    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRoles());
    }

    public String getUserRole(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRoles().iterator().next();
    }

    public void updateUserLocation(String userId, String newLocation) {
        User user = userRepository.findById(new ObjectId(userId))  // ✅ Convert ID to ObjectId
                .orElseThrow(() -> new RuntimeException("User not found"));

        double[] coordinates = mapboxUtil.getCoordinates(newLocation);
        user.setLocation(newLocation);
        user.setLatitude(coordinates[0]);
        user.setLongitude(coordinates[1]);

        userRepository.save(user);
    }
}
