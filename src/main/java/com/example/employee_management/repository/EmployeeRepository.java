
package com.example.employee_management.repository;

import com.example.employee_management.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 1. Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    // 2. Tìm nhân viên theo tên hoặc email
    List<Employee> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String fullName,
            String email
    );

    // 3. Lọc nhân viên theo phòng ban
    List<Employee> findByDepartment_Id(Long departmentId);

    // 4. Lọc nhân viên theo chức vụ
    List<Employee> findByPositionContainingIgnoreCase(String position);

    // 5. Phân trang danh sách nhân viên
    Page<Employee> findAll(Pageable pageable);

    // 6. Lấy danh sách nhân viên đang làm việc
    List<Employee> findByActiveTrue();

    // 7. Đếm số nhân viên đang làm việc
    long countByActiveTrue();
}
