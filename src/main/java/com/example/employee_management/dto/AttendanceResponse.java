
package com.example.employee_management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceResponse {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private String departmentName;

    private LocalDate attendanceDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private String status;
    private String checkoutStatus;

    private Long workedHours;
    private Long remainingMinutes;
    private Long lateMinutes;
    private Long earlyLeaveMinutes;

    public AttendanceResponse() {
    }

    public AttendanceResponse(
            Long id,
            Long employeeId,
            String employeeName,
            String departmentName,
            LocalDate attendanceDate,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String status,
            String checkoutStatus,
            Long workedHours,
            Long remainingMinutes,
            Long lateMinutes,
            Long earlyLeaveMinutes) {

        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.attendanceDate = attendanceDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.checkoutStatus = checkoutStatus;
        this.workedHours = workedHours;
        this.remainingMinutes = remainingMinutes;
        this.lateMinutes = lateMinutes;
        this.earlyLeaveMinutes = earlyLeaveMinutes;
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public String getStatus() {
        return status;
    }

    public String getCheckoutStatus() {
        return checkoutStatus;
    }

    public Long getWorkedHours() {
        return workedHours;
    }

    public Long getRemainingMinutes() {
        return remainingMinutes;
    }

    public Long getLateMinutes() {
        return lateMinutes;
    }

    public Long getEarlyLeaveMinutes() {
        return earlyLeaveMinutes;
    }
}
