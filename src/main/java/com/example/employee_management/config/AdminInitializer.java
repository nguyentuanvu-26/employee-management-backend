// khởi tạo tk admin
package com.example.employee_management.config;

import com.example.employee_management.entity.AppUser;
import com.example.employee_management.repository.AppUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:ChangeMe123!}")
    private String adminPassword;

    public AdminInitializer(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (appUserRepository.existsByUsername(adminUsername)) {
            System.out.println("Tai khoan Admin da ton tai.");
            return;
        }

        AppUser admin = new AppUser();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");

        appUserRepository.save(admin);

        System.out.println("Da khoi tao tai khoan Admin.");
    }
}
