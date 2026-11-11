
package com.example.employee_management.service;

import com.example.employee_management.entity.AppUser;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.AppUserRepository;
import com.example.employee_management.repository.EmployeeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(
            AppUserRepository appUserRepository,
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Admin tao tai khoan cho nhan vien
    @Transactional
    public AppUser createEmployeeAccount(
            Long employeeId,
            String username,
            String rawPassword) {

        String normalizedUsername = username.trim();

        // Kiem tra nhan vien co ton tai khong
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay nhan vien co ID: " + employeeId
                ));

        // Kiem tra username da duoc su dung chua
        if (appUserRepository.existsByUsername(normalizedUsername)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username da ton tai"
            );
        }

        // Moi nhan vien chi duoc lien ket mot tai khoan
        if (appUserRepository.existsByEmployee_Id(employeeId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nhan vien nay da co tai khoan"
            );
        }

        AppUser user = new AppUser();

        user.setUsername(normalizedUsername);

        // Ma hoa mat khau bang BCrypt truoc khi luu
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Tai khoan nhan vien mac dinh co role EMPLOYEE
        user.setRole("EMPLOYEE");

        // Lien ket tai khoan voi nhan vien
        user.setEmployee(employee);

        return appUserRepository.save(user);
    }

    // Admin lay danh sach tat ca tai khoan
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllAccounts() {

        List<AppUser> users = appUserRepository.findAll();

        List<Map<String, Object>> result = new ArrayList<>();

        for (AppUser user : users) {

            Map<String, Object> account = new LinkedHashMap<>();

            account.put("id", user.getId());
            account.put("username", user.getUsername());
            account.put("role", user.getRole());

            // Lay thong tin nhan vien duoc lien ket
            if (user.getEmployee() != null) {

                account.put("employeeId", user.getEmployee().getId());

                account.put(
                        "employeeName",
                        user.getEmployee().getFullName()
                );

            } else {

                // Tai khoan Admin khong gan voi nhan vien
                account.put("employeeId", null);
                account.put("employeeName", "Quản trị viên");
            }

            result.add(account);
        }

        return result;
    }
}
