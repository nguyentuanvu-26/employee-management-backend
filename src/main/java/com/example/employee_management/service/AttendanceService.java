
package com.example.employee_management.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.employee_management.dto.AttendanceResponse;
import com.example.employee_management.entity.Attendance;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.AttendanceRepository;
import com.example.employee_management.repository.EmployeeRepository;

@Service
public class AttendanceService {

    private static final ZoneId VIETNAM_ZONE =
            ZoneId.of("Asia/Ho_Chi_Minh");

    private static final LocalTime WORK_START = LocalTime.of(8, 0);
    private static final LocalTime WORK_END = LocalTime.of(17, 0);

    private static final LocalTime LUNCH_START = LocalTime.of(12, 0);
    private static final LocalTime LUNCH_END = LocalTime.of(13, 0);

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository) {

        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // 1. CHECK-IN
    public Attendance checkIn(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy nhân viên"));

        LocalDate today = LocalDate.now(VIETNAM_ZONE);
//kiemr tra xem đã chấm công hôm nay chưa
        if (attendanceRepository
                .existsByEmployee_IdAndAttendanceDate(employeeId, today)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nhân viên đã chấm công hôm nay");
        }

        LocalDateTime now = LocalDateTime.now(VIETNAM_ZONE);

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(today);
        attendance.setCheckIn(now);

        // Phân loại đúng giờ hoặc đi muộn
        if (now.toLocalTime().isAfter(WORK_START)) {
            attendance.setStatus("LATE");
        } else {
            attendance.setStatus("PRESENT");
        }

        // Đã check-in nhưng chưa check-out
        attendance.setCheckoutStatus("INCOMPLETE");

        return attendanceRepository.save(attendance);
    }

    // 2. CHECK-OUT
    public Attendance checkOut(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy nhân viên"));

        LocalDate today = LocalDate.now(VIETNAM_ZONE);

        Attendance attendance = attendanceRepository
                .findByEmployee_IdAndAttendanceDate(employeeId, today)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nhân viên chưa chấm công vào hôm nay"));

        if (attendance.getCheckOut() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nhân viên đã chấm công ra hôm nay");
        }

        attendance.setCheckOut(LocalDateTime.now(VIETNAM_ZONE));

        // Đánh dấu đã checkout
        attendance.setCheckoutStatus("COMPLETED");

        return attendanceRepository.save(attendance);
    }

    // 3. LẤY TOÀN BỘ LỊCH SỬ
    public List<AttendanceResponse> getAllAttendance() {

        List<Attendance> attendances =
                attendanceRepository.findAllByOrderByAttendanceDateDesc();

        return attendances.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 4. LẤY LỊCH SỬ THEO NHÂN VIÊN
    public List<AttendanceResponse> getAttendanceByEmployee(
            Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy nhân viên"));

        List<Attendance> attendances =
                attendanceRepository
                        .findAllByEmployee_IdOrderByAttendanceDateDesc(
                                employeeId);

        return attendances.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 5. LẤY LỊCH SỬ THEO NGÀY
    public List<AttendanceResponse> getAttendanceByDate(
            LocalDate date) {

        List<Attendance> attendances =
                attendanceRepository.findAllByAttendanceDate(date);

        return attendances.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 6. CHUYỂN ENTITY THÀNH DTO
    private AttendanceResponse convertToResponse(
            Attendance attendance) {

        Long totalWorkedMinutes =
                calculateWorkedMinutes(attendance);// tổng số phút làm việc

        Long workedHours = totalWorkedMinutes == null
                ? null : totalWorkedMinutes / 60;//chuyển phút -> giờ

        Long remainingMinutes = totalWorkedMinutes == null
                ? null : totalWorkedMinutes % 60;//lấy số phút còn lại
// 2 hàm này tính số phút đi muộn và về sớm
        Long lateMinutes = calculateLateMinutes(attendance);

        Long earlyLeaveMinutes =
                calculateEarlyLeaveMinutes(attendance);
// chuyển từ entity sang dto để API trả về
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getEmployee().getId(),
                attendance.getEmployee().getFullName(),

                attendance.getEmployee().getDepartment() != null
                        ? attendance.getEmployee()
                                .getDepartment()
                                .getDepartmentName()
                        : null,

                attendance.getAttendanceDate(),
                attendance.getCheckIn(),
                attendance.getCheckOut(),
                attendance.getStatus(),
                attendance.getCheckoutStatus(),
                workedHours,
                remainingMinutes,
                lateMinutes,
                earlyLeaveMinutes
        );
    }

    // 7. TÍNH TỔNG THỜI GIAN LÀM VIỆC
    private Long calculateWorkedMinutes(Attendance attendance) {

        LocalDateTime checkIn = attendance.getCheckIn();
        LocalDateTime checkOut = attendance.getCheckOut();

        if (checkIn == null || checkOut == null) {
            return null;
        }

        if (checkOut.isBefore(checkIn)) {
            return 0L;
        }

        long totalMinutes =
                Duration.between(checkIn, checkOut).toMinutes();

        LocalDate date = attendance.getAttendanceDate();

        LocalDateTime lunchStart = date.atTime(LUNCH_START);
        LocalDateTime lunchEnd = date.atTime(LUNCH_END);

        LocalDateTime overlapStart =
                checkIn.isAfter(lunchStart) ? checkIn : lunchStart;

        LocalDateTime overlapEnd =
                checkOut.isBefore(lunchEnd) ? checkOut : lunchEnd;

        if (overlapEnd.isAfter(overlapStart)) {

            long lunchMinutes =
                    Duration.between(
                            overlapStart,
                            overlapEnd).toMinutes();

            totalMinutes -= lunchMinutes;
        }

        return Math.max(totalMinutes, 0);
    }

    // 8. TÍNH PHÚT ĐI MUỘN
    private Long calculateLateMinutes(Attendance attendance) {

        LocalDateTime checkIn = attendance.getCheckIn();

        if (checkIn == null) {
            return null;
        }

        LocalDateTime workStart =
                attendance.getAttendanceDate().atTime(WORK_START);

        if (!checkIn.isAfter(workStart)) {
            return 0L;
        }

        return Duration.between(
                workStart,
                checkIn).toMinutes();
    }

    // 9. TÍNH PHÚT VỀ SỚM
    private Long calculateEarlyLeaveMinutes(
            Attendance attendance) {

        LocalDateTime checkOut = attendance.getCheckOut();

        if (checkOut == null) {
            return null;
        }

        LocalDateTime workEnd =
                attendance.getAttendanceDate().atTime(WORK_END);

        if (!checkOut.isBefore(workEnd)) {
            return 0L;
        }

        return Duration.between(
                checkOut,
                workEnd).toMinutes();
    }
}
