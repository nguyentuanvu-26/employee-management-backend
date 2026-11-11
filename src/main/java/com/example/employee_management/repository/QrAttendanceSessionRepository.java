package com.example.employee_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee_management.entity.QrAttendanceSession;

@Repository
public interface QrAttendanceSessionRepository
        extends JpaRepository<QrAttendanceSession, Long> {

    Optional<QrAttendanceSession> findByToken(String token);

}