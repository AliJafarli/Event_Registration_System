package com.codealpha.eventreg.service;

import com.codealpha.eventreg.domain.User;
import com.codealpha.eventreg.dto.LoginRequest;
import com.codealpha.eventreg.dto.RegisterRequest;
import com.codealpha.eventreg.dto.TokenResponse;
import com.codealpha.eventreg.exception.ApiExceptions;
import com.codealpha.eventreg.repo.UserRepository;
import com.codealpha.eventreg.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder encoder,
                       AuthenticationManager authManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail().toLowerCase())) {
            throw new ApiExceptions.Conflict("Email already registered");
        }
        User u = new User();
        u.setEmail(req.getEmail().toLowerCase());
        u.setFullName(req.getFullName());
        u.setRole(User.Role.USER);
        u.setPasswordHash(encoder.encode(req.getPassword()));
        userRepository.save(u);
    }

    public TokenResponse login(LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase(), req.getPassword())
            );
            var principal = (com.codealpha.eventreg.security.UserPrincipal) auth.getPrincipal();
            User user = principal.getUser();
            String token = jwtService.createAccessToken(user);
            return TokenResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .expiresInSeconds(jwtService.getAccessTokenExpiresInSeconds())
                    .role(user.getRole().name())
                    .build();
        } catch (BadCredentialsException ex) {
            throw new ApiExceptions.Unauthorized("Invalid credentials");
        }
    }
}
