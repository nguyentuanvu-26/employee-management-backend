
package com.example.employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_management.dto.DashboardResponse;
import com.example.employee_management.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    // Constructor Injection
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // API lấy dữ liệu thống kê Dashboard
    @GetMapping
    public DashboardResponse getDashboardStatistics() {
        return dashboardService.getDashboardStatistics();
    }
}
