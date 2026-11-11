// tiếp nhận yêu cầu quản lý phòng ban
package com.example.employee_management.controller;

import com.example.employee_management.entity.Department;
import com.example.employee_management.repository.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    // 1. Lấy danh sách phòng ban
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // 2. Lấy phòng ban theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(
            @PathVariable Long id) {

        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Thêm phòng ban
    @PostMapping
    public Department createDepartment(
            @RequestBody Department department) {

        department.setId(null);
        return departmentRepository.save(department);
    }

    // 4. Sửa phòng ban
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department request) {

        return departmentRepository.findById(id)
                .map(department -> {
                    department.setDepartmentName(
                            request.getDepartmentName());
                    return ResponseEntity.ok(
                            departmentRepository.save(department));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Xóa phòng ban
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long id) {

        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
