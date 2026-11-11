package com.example.employee_management.dto;

import java.time.LocalDateTime;

public class QrCodeResponse {

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean active;

    public QrCodeResponse() {
    }

    public QrCodeResponse(
            String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            boolean active) {

        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}