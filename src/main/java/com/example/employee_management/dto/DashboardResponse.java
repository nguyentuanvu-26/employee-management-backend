
package com.example.employee_management.dto;

public class DashboardResponse {

    private long totalEmployees;
    private long activeEmployees;
    private long lateToday;
    private long absentToday;

    public DashboardResponse() {
    }

    public DashboardResponse(
            long totalEmployees,
            long activeEmployees,
            long lateToday,
            long absentToday
    ) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.lateToday = lateToday;
        this.absentToday = absentToday;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(long activeEmployees) {
        this.activeEmployees = activeEmployees;
    }

    public long getLateToday() {
        return lateToday;
    }

    public void setLateToday(long lateToday) {
        this.lateToday = lateToday;
    }

    public long getAbsentToday() {
        return absentToday;
    }

    public void setAbsentToday(long absentToday) {
        this.absentToday = absentToday;
    }
}
