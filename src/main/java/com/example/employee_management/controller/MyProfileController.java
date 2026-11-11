
package com.example.employee_management.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.employee_management.entity.AppUser;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.AppUserRepository;

@RestController
@RequestMapping("/api/me")
public class MyProfileController {

    private final AppUserRepository appUserRepository;

    public MyProfileController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Map<String, Object> getMyProfile(Authentication authentication) {

        // Lay username cua tai khoan dang dang nhap
        String username = authentication.getName();

        // Tim tai khoan trong database
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay tai khoan"));

        // Lay nhan vien lien ket voi tai khoan
        Employee employee = user.getEmployee();

        // Tao response
        Map<String, Object> response = new LinkedHashMap<>();

        // Thong tin tai khoan
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        // Thong tin nhan vien
        // Neu la ADMIN khong lien ket Employee thi cac gia tri la null
        response.put("id",
                employee == null ? null : employee.getId());

        response.put("fullName",
                employee == null ? null : employee.getFullName());

        response.put("email",
                employee == null ? null : employee.getEmail());

        response.put("phone",
                employee == null ? null : employee.getPhone());

        response.put("position",
                employee == null ? null : employee.getPosition());

        response.put("departmentName",
                employee == null || employee.getDepartment() == null
                        ? null
                        : employee.getDepartment().getDepartmentName());

        return response;
    }
}
