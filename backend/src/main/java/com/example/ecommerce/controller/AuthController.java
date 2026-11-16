package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AuthDtos.*;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder,
                          AuthenticationManager authenticationManager, JwtService jwt) {
        this.users = users; this.encoder = encoder;
        this.authenticationManager = authenticationManager; this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "An account with this email already exists."));
        User user = users.save(new User(request.name().trim(), request.email().trim(),
                encoder.encode(request.password()), "USER"));
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        UserDetails details = (UserDetails) authentication.getPrincipal();
        User user = users.findByEmailIgnoreCase(details.getUsername()).orElseThrow();
        return ResponseEntity.ok(new AuthResponse(jwt.generateToken(details), "Bearer",
                user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }
}
