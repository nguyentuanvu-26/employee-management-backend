// tiếp nhận yêu cầu quản lý nhân viên
package com.example.employee_management.controller;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Lấy toàn bộ nhân viên
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Tìm kiếm theo tên hoặc email
    @GetMapping("/search")
    public List<Employee> searchEmployees(
            @RequestParam String keyword) {
        return employeeService.searchEmployees(keyword);
    }

    // Lọc theo phòng ban
    @GetMapping("/department/{departmentId}")
    public List<Employee> getEmployeesByDepartment(
            @PathVariable Long departmentId) {
        return employeeService.getEmployeesByDepartment(departmentId);
    }

    // Lọc theo chức vụ
    @GetMapping("/position")
    public List<Employee> getEmployeesByPosition(
            @RequestParam("position") String position) {
        return employeeService.getEmployeesByPosition(position);
    }

    // PHÂN TRANG NHÂN VIÊN
    @GetMapping("/page")
    public Page<Employee> getEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return employeeService.getEmployeesPaginated(page, size);
    }

    // Thêm nhân viên
    @PostMapping
    public Employee createEmployee(
            @Valid @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    // Tìm theo ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(
            @PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    // Cập nhật nhân viên
    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employeeDetails) {
        return employeeService.updateEmployee(id, employeeDetails);
    }

    // Xóa nhân viên
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }
}
