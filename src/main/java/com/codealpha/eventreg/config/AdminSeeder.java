package com.codealpha.eventreg.config;

import com.codealpha.eventreg.domain.User;
import com.codealpha.eventreg.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${app.seed-admin.enabled:true}")
    private boolean enabled;

    @Value("${app.seed-admin.email:admin@example.com}")
    private String email;

    @Value("${app.seed-admin.password:Admin123!}")
    private String password;

    @Value("${app.seed-admin.full-name:System Admin}")
    private String fullName;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!enabled) return;
        if (userRepository.existsByEmail(email.toLowerCase())) return;

        User admin = new User();
        admin.setEmail(email.toLowerCase());
        admin.setFullName(fullName);
        admin.setRole(User.Role.ADMIN);
        admin.setPasswordHash(encoder.encode(password));
        userRepository.save(admin);
    }
}
