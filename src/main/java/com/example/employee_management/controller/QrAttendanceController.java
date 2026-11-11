
package com.example.employee_management.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.employee_management.dto.QrCodeResponse;
import com.example.employee_management.dto.QrScanRequest;
import com.example.employee_management.dto.QrScanResponse;

import com.example.employee_management.entity.QrAttendanceSession;

import com.example.employee_management.service.QrAttendanceService;

@RestController
@RequestMapping("/api/qr-attendance")
public class QrAttendanceController {

    private final QrAttendanceService qrAttendanceService;

    public QrAttendanceController(
            QrAttendanceService qrAttendanceService) {

        this.qrAttendanceService = qrAttendanceService;
    }

    // ADMIN tạo mã QR mới
    @PostMapping("/create")
    public QrCodeResponse createQr() {

        QrAttendanceSession session =
                qrAttendanceService.createQrSession();

        return new QrCodeResponse(
                session.getToken(),
                session.getCreatedAt(),
                session.getExpiresAt(),
                session.isActive()
        );
    }

    // EMPLOYEE quét mã QR
    @PostMapping("/scan")
    public QrScanResponse scanQr(
            @RequestBody QrScanRequest request,
            Authentication authentication) {

        return qrAttendanceService.scanQr(
                request.getToken(),
                authentication
        );
    }
}
