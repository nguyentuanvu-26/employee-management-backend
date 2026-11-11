
package com.example.employee_management.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;

import com.example.employee_management.entity.Attendance;
import com.example.employee_management.entity.AppUser;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.entity.QrAttendanceSession;

import com.example.employee_management.dto.QrScanResponse;

import com.example.employee_management.repository.QrAttendanceSessionRepository;
import com.example.employee_management.repository.AppUserRepository;
import com.example.employee_management.repository.AttendanceRepository;

@Service
public class QrAttendanceService {

    private final QrAttendanceSessionRepository qrRepository;
    private final AppUserRepository appUserRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceService attendanceService;

    private final SecureRandom secureRandom = new SecureRandom();

    private static final ZoneId VIETNAM_ZONE =
            ZoneId.of("Asia/Ho_Chi_Minh");

    public QrAttendanceService(
            QrAttendanceSessionRepository qrRepository,
            AppUserRepository appUserRepository,
            AttendanceRepository attendanceRepository,
            AttendanceService attendanceService) {

        this.qrRepository = qrRepository;
        this.appUserRepository = appUserRepository;
        this.attendanceRepository = attendanceRepository;
        this.attendanceService = attendanceService;
    }

    // Tạo mã token ngẫu nhiên
    private String generateToken() {

        byte[] randomBytes = new byte[32];

        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    // Tạo phiên QR mới
    @Transactional
    public QrAttendanceSession createQrSession() {

        LocalDateTime now = LocalDateTime.now(VIETNAM_ZONE);

        // Vô hiệu hóa các mã QR cũ
        qrRepository.findAll().forEach(session -> {
            if (session.isActive()) {
                session.setActive(false);
            }
        });

        QrAttendanceSession session = new QrAttendanceSession();

        session.setToken(generateToken());
        session.setCreatedAt(now);
        session.setExpiresAt(now.plusSeconds(30));
        session.setActive(true);

        return qrRepository.save(session);
    }

    // Kiểm tra token QR
    public QrAttendanceSession validateQrToken(String token) {

        QrAttendanceSession session = qrRepository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Mã QR không hợp lệ"));

        if (!session.isActive()) {
            throw new RuntimeException("Mã QR đã hết hiệu lực");
        }

        LocalDateTime now = LocalDateTime.now(VIETNAM_ZONE);

        if (!now.isBefore(session.getExpiresAt())) {
            throw new RuntimeException("Mã QR đã hết hạn");
        }

        return session;
    }

    // Nhân viên quét QR để chấm công
    @Transactional
    public QrScanResponse scanQr(
            String token,
            Authentication authentication) {

        // 1. Kiểm tra mã QR
        validateQrToken(token);

        // 2. Lấy tài khoản đang đăng nhập
        String username = authentication.getName();

        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy tài khoản"));

        // 3. Lấy nhân viên liên kết với tài khoản
        Employee employee = appUser.getEmployee();

        if (employee == null) {
            throw new RuntimeException(
                    "Tài khoản chưa được liên kết với nhân viên");
        }

        if (!employee.isActive()) {
            throw new RuntimeException(
                    "Tài khoản nhân viên đã ngừng hoạt động");
        }

        Long employeeId = employee.getId();

        // 4. Kiểm tra chấm công hôm nay
        LocalDate today = LocalDate.now(VIETNAM_ZONE);

        Attendance attendance = attendanceRepository
                .findByEmployee_IdAndAttendanceDate(employeeId, today)
                .orElse(null);

        // 5. Chưa có bản ghi: thực hiện check-in
        if (attendance == null) {

            attendanceService.checkIn(employeeId);

            return new QrScanResponse(
                    "Chấm công vào thành công",
                    "CHECK_IN",
                    employee.getFullName()
            );
        }

        // 6. Đã check-in nhưng chưa check-out
        if (attendance.getCheckOut() == null) {

            attendanceService.checkOut(employeeId);

            return new QrScanResponse(
                    "Chấm công ra thành công",
                    "CHECK_OUT",
                    employee.getFullName()
            );
        }

        // 7. Đã hoàn thành chấm công trong ngày
        throw new RuntimeException(
                "Bạn đã hoàn thành chấm công hôm nay");
    }
}
