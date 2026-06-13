package com.career.career_guidance.controller;

import com.career.career_guidance.model.User;
import com.career.career_guidance.security.JwtUtil;
import com.career.career_guidance.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
public ResponseEntity<String> registerUser(@RequestBody User user) {

    try {
        userService.registerUser(user);
        return ResponseEntity.ok("Registration Successful");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }

    @GetMapping("/logs")
    public Object getAllLogs(@RequestHeader("Authorization") String token) {

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return "Invalid JWT Token";
        }

        String role = jwtUtil.getRoleFromToken(token);

        if (role.equalsIgnoreCase("ADMIN")) {
            return userService.getAllLogs();
        } else {
            return "Access Denied: Admins only";
        }
    }

    @GetMapping("/users")
    public Object getAllUsers(@RequestHeader("Authorization") String token) {

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return "Invalid JWT Token";
        }

        String role = jwtUtil.getRoleFromToken(token);

        if (role.equalsIgnoreCase("ADMIN")) {
            return userService.getAllUsers();
        } else {
            return "Access Denied: Admins only";
        }
    }

    @PostMapping("/unlock/{username}")
    public Object unlockUser(
            @RequestHeader("Authorization") String token,
            @PathVariable String username) {

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return "Invalid JWT Token";
        }

        String role = jwtUtil.getRoleFromToken(token);

        if (role.equalsIgnoreCase("ADMIN")) {
            return userService.unlockUser(username);
        } else {
            return "Access Denied: Admins only";
        }
    }
    @PostMapping("/reset-password")
public String resetPassword(@RequestBody User user) {
    return userService.resetPassword(user.getEmail(), user.getPassword());
}

@PostMapping("/forgot-password")
public String forgotPassword(@RequestBody User user) {
    return userService.sendResetPasswordEmail(user.getEmail());
}
}