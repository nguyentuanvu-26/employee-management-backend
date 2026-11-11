
package com.example.employee_management.service;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.example.employee_management.dto.DashboardResponse;
import com.example.employee_management.repository.EmployeeRepository;
import com.example.employee_management.repository.AttendanceRepository;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    // Constructor Injection
    public DashboardService(
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // Lấy dữ liệu thống kê cho Dashboard
    public DashboardResponse getDashboardStatistics() {

        // Lấy ngày hiện tại theo múi giờ Việt Nam
        LocalDate today = LocalDate.now(
                ZoneId.of("Asia/Ho_Chi_Minh")
        );

        // 1. Tổng số nhân viên
        long totalEmployees = employeeRepository.count();

        // 2. Số nhân viên đang làm việc
        long activeEmployees =
                employeeRepository.countByActiveTrue();

        // 3. Số nhân viên đi muộn hôm nay
        long lateToday =
                attendanceRepository.countByAttendanceDateAndStatus(
                        today,
                        "LATE"
                );

        // 4. Số nhân viên vắng mặt hôm nay
        long absentToday =
                attendanceRepository.countByAttendanceDateAndStatus(
                        today,
                        "ABSENT"
                );

        // Đóng gói dữ liệu và trả về
        return new DashboardResponse(
                totalEmployees,
                activeEmployees,
                lateToday,
                absentToday
        );
    }
}
