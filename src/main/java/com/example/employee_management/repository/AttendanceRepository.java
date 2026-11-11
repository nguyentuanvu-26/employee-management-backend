
package com.example.employee_management.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee_management.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 1. Kiểm tra nhân viên đã chấm công trong ngày chưa
    boolean existsByEmployee_IdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    // 2. Tìm bản ghi chấm công theo nhân viên và ngày
    Optional<Attendance> findByEmployee_IdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    // 3. Lấy toàn bộ lịch sử, ngày mới nhất lên trước
    List<Attendance> findAllByOrderByAttendanceDateDesc();

    // 4. Lấy lịch sử chấm công theo nhân viên
    List<Attendance> findAllByEmployee_IdOrderByAttendanceDateDesc(
            Long employeeId
    );

    // 5. Lấy lịch sử chấm công theo ngày
    List<Attendance> findAllByAttendanceDate(
            LocalDate attendanceDate
    );

    // 6. Đếm số nhân viên theo trạng thái chấm công trong ngày
    long countByAttendanceDateAndStatus(
            LocalDate attendanceDate,
            String status
    );
}
