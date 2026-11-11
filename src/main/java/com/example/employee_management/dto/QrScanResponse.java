package com.example.employee_management.dto;

public class QrScanResponse {

    private String message;
    private String attendanceType;
    private String employeeName;

    public QrScanResponse() {
    }

    public QrScanResponse(
            String message,
            String attendanceType,
            String employeeName) {

        this.message = message;
        this.attendanceType = attendanceType;
        this.employeeName = employeeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}