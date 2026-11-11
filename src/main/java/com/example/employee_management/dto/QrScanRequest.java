package com.example.employee_management.dto;

public class QrScanRequest {

    private String token;

    public QrScanRequest() {
    }

    public QrScanRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}