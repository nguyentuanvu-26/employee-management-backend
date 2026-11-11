
package com.example.employee_management.controller;

import com.example.employee_management.dto.CreateEmployeeAccountRequest;
import com.example.employee_management.entity.AppUser;
import com.example.employee_management.service.AppUserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    // Admin cap tai khoan cho nhan vien
    @PostMapping("/employee")
    public ResponseEntity<Map<String, Object>> createEmployeeAccount(
            @Valid @RequestBody CreateEmployeeAccountRequest request) {

        AppUser user = appUserService.createEmployeeAccount(
                request.getEmployeeId(),
                request.getUsername(),
                request.getPassword()
        );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("message", "Tao tai khoan nhan vien thanh cong");
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("employeeId", request.getEmployeeId());

        // Khong tra password hoac chuoi BCrypt
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Admin xem danh sach tat ca tai khoan
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllAccounts() {

        List<Map<String, Object>> accounts =
                appUserService.getAllAccounts();

        return ResponseEntity.ok(accounts);
    }
}
