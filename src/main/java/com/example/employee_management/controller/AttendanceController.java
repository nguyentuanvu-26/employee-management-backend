
package com.example.employee_management.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.employee_management.dto.AttendanceResponse;
import com.example.employee_management.entity.Attendance;
import com.example.employee_management.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService) {

        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in/{employeeId}")
    public Attendance checkIn(
            @PathVariable Long employeeId) {

        return attendanceService.checkIn(employeeId);
    }

    @PostMapping("/check-out/{employeeId}")
    public Attendance checkOut(
            @PathVariable Long employeeId) {

        return attendanceService.checkOut(employeeId);
    }

    @GetMapping
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceService.getAllAttendance();
    }

    @GetMapping("/employee/{employeeId}")
    public List<AttendanceResponse> getAttendanceByEmployee(
            @PathVariable Long employeeId) {

        return attendanceService.getAttendanceByEmployee(employeeId);
    }

    @GetMapping("/date")
    public List<AttendanceResponse> getAttendanceByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return attendanceService.getAttendanceByDate(date);
    }
}
