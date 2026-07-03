package com.example.notification_service.controller;

import com.example.notification_service.dto.AuthRequest;
import com.example.notification_service.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // 🚨 BULLETPROOF CORS FIX: Forces the browser to allow this request
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        System.out.println("\n--- 🚨 LOGIN ATTEMPT 🚨 ---");
        System.out.println("Username received: " + request.getUsername());
        System.out.println("Password received: " + request.getPassword());

        try {
            UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                System.out.println("✅ Result: SUCCESS! Token generated.");

                // Return a proper JSON object to React
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                System.out.println("❌ Result: FAILED! Passwords do not match.");
                return ResponseEntity.status(401).body("Invalid password");
            }
        } catch (Exception e) {
            System.out.println("❌ Result: FAILED! Error: " + e.getMessage());
            return ResponseEntity.status(401).body("User not found or error occurred");
        }
    }
}