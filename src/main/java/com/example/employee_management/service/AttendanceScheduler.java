
package com.example.employee_management.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee_management.entity.Attendance;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.AttendanceRepository;
import com.example.employee_management.repository.EmployeeRepository;

@Component
public class AttendanceScheduler {

    private static final ZoneId VIETNAM_ZONE =
            ZoneId.of("Asia/Ho_Chi_Minh");

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceScheduler(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository) {

        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Tự động chạy lúc 17:05 từ thứ Hai đến thứ Sáu
    @Scheduled(
        cron = "0 5 17 * * MON-FRI",
        zone = "Asia/Ho_Chi_Minh"
    )
    @Transactional
    public void markAbsentEmployees() {

        LocalDate today = LocalDate.now(VIETNAM_ZONE);

        // Không xử lý vào thứ Bảy và Chủ nhật
        DayOfWeek day = today.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY ||
            day == DayOfWeek.SUNDAY) {

            return;
        }

        // Lấy danh sách nhân viên đang làm việc
        List<Employee> employees =
                employeeRepository.findByActiveTrue();

        List<Attendance> absentRecords = new ArrayList<>();

        for (Employee employee : employees) {

            // Kiểm tra nhân viên đã có bản ghi hôm nay chưa
            boolean alreadyHasAttendance =
                    attendanceRepository
                    .existsByEmployee_IdAndAttendanceDate(
                            employee.getId(), today);

            // Nếu chưa có bản ghi thì đánh dấu vắng mặt
            if (!alreadyHasAttendance) {

                Attendance attendance = new Attendance();

                attendance.setEmployee(employee);
                attendance.setAttendanceDate(today);
                attendance.setCheckIn(null);
                attendance.setCheckOut(null);

                attendance.setStatus("ABSENT");
                attendance.setCheckoutStatus("NOT_APPLICABLE");

                absentRecords.add(attendance);
            }
        }

        // Lưu các bản ghi vắng mặt
        if (!absentRecords.isEmpty()) {
            attendanceRepository.saveAll(absentRecords);
        }

        System.out.println("===== ATTENDANCE SCHEDULER =====");
        System.out.println("Ngay chay: " + today);
        System.out.println(
                "So nhan vien dang lam viec: " + employees.size()
        );
        System.out.println(
                "So nhan vien vang mat: " + absentRecords.size()
        );
        System.out.println("================================");
    }
}
