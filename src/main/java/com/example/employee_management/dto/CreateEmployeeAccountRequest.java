// nhận và kiểm soát dữ liệu đầu vào của nhân viên
package com.example.employee_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateEmployeeAccountRequest {

    @NotNull(message = "Employee ID khong duoc de trong")
    private Long employeeId;

    @NotBlank(message = "Username khong duoc de trong")
    @Size(max = 50, message = "Username toi da 50 ky tu")
    private String username;

    @NotBlank(message = "Password khong duoc de trong")
    @Size(min = 8, max = 72,
          message = "Password phai co tu 8 den 72 ky tu")
    private String password;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
