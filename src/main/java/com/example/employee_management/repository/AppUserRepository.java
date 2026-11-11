// phụ trách các thao tác với bảng tài khoản đăng nhập.
package com.example.employee_management.repository;

import com.example.employee_management.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    // Kiem tra nhan vien da duoc cap tai khoan chua
    boolean existsByEmployee_Id(Long employeeId);
}
