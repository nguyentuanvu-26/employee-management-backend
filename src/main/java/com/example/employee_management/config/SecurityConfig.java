
package com.example.employee_management.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    // Ma hoa mat khau bang BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cau hinh CORS cho React Frontend (HTTP va HTTPS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:5173",
                        "http://192.168.1.9:5173",
                        "https://localhost:5173",
                        "https://192.168.1.9:5173"
                )
        );

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                Arrays.asList(
                        "Authorization",
                        "Content-Type"
                )
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // Phan quyen truy cap cac API
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/me")
                .hasAnyRole("ADMIN", "EMPLOYEE")

                .requestMatchers("/api/users/**")
                .hasRole("ADMIN")

                .requestMatchers("/api/employees/**")
                .hasRole("ADMIN")

                .requestMatchers("/api/attendance/employee/**")
                .hasRole("EMPLOYEE")

                .requestMatchers("/api/qr-attendance/create")
                .hasRole("ADMIN")

                .requestMatchers("/api/qr-attendance/scan")
                .hasRole("EMPLOYEE")

                .anyRequest()
                .hasRole("ADMIN")
            )

            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
