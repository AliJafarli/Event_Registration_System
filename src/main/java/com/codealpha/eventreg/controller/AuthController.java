package com.codealpha.eventreg.controller;

import com.codealpha.eventreg.dto.LoginRequest;
import com.codealpha.eventreg.dto.RegisterRequest;
import com.codealpha.eventreg.dto.TokenResponse;
import com.codealpha.eventreg.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
