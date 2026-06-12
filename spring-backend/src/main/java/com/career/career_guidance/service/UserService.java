package com.career.career_guidance.service;

import com.career.career_guidance.model.LoginLog;
import com.career.career_guidance.model.User;
import com.career.career_guidance.repository.LoginLogRepository;
import com.career.career_guidance.repository.UserRepository;
import com.career.career_guidance.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            throw new RuntimeException("Username already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        user.setFailedAttempts(0);
        user.setAccountLocked(false);

        User savedUser = userRepository.save(user);

        emailService.sendWelcomeEmail(
                savedUser.getEmail(),
                savedUser.getUsername()
        );

        return savedUser;
    }

    public String loginUser(User user) {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser == null) {
            loginLogRepository.save(
                    new LoginLog(user.getUsername(), "FAILED", LocalDateTime.now())
            );

            return "User not found";
        }

        if (existingUser.isAccountLocked()) {
            loginLogRepository.save(
                    new LoginLog(user.getUsername(), "ACCOUNT_LOCKED", LocalDateTime.now())
            );

            return "Account is locked";
        }

        boolean passwordMatched = passwordEncoder.matches(
                user.getPassword(),
                existingUser.getPassword()
        );

        if (passwordMatched) {

            existingUser.setFailedAttempts(0);
            userRepository.save(existingUser);

            loginLogRepository.save(
                    new LoginLog(user.getUsername(), "SUCCESS", LocalDateTime.now())
            );

            return jwtUtil.generateToken(
                    existingUser.getUsername(),
                    existingUser.getRole()
            );

        } else {

            int attempts = existingUser.getFailedAttempts() + 1;

            existingUser.setFailedAttempts(attempts);

            if (attempts >= 3) {
                existingUser.setAccountLocked(true);
            }

            userRepository.save(existingUser);

            loginLogRepository.save(
                    new LoginLog(user.getUsername(), "FAILED", LocalDateTime.now())
            );

            return "Invalid Username or Password";
        }
    }

    public List<LoginLog> getAllLogs() {
        return loginLogRepository.findAll();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String unlockUser(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "User not found";
        }

        user.setFailedAttempts(0);
        user.setAccountLocked(false);

        userRepository.save(user);

        return "Account unlocked successfully";
    }

    public String resetPassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "Email ID not found";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFailedAttempts(0);
        user.setAccountLocked(false);

        userRepository.save(user);

        return "Password reset successfully. Please login.";
    }

    public String sendResetPasswordEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "Email ID not found";
        }

        emailService.sendResetPasswordEmail(email);

        return "Password reset email sent successfully.";
    }
}