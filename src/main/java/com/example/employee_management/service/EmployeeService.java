// xử lý các công việc liên quan đến nhân viên.
package com.example.employee_management.service;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeRepository;
import com.example.employee_management.repository.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // 1. Lấy toàn bộ nhân viên
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // 2. Kiểm tra Department có tồn tại không
    private void validateDepartment(Employee employee) {
        if (employee.getDepartment() == null
                || employee.getDepartment().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Department khong duoc de trong"
            );
        }

        Long departmentId = employee.getDepartment().getId();

        if (!departmentRepository.existsById(departmentId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Department khong ton tai voi ID: " + departmentId
            );
        }
    }

    // 3. Thêm nhân viên
    public Employee createEmployee(Employee employee) {

        // Kiểm tra Department trước khi lưu
        validateDepartment(employee);

        // Kiểm tra email đã tồn tại chưa
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email nay da duoc su dung"
            );
        }

        return employeeRepository.save(employee);
    }

    // 4. Tìm nhân viên theo ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay nhan vien co ID: " + id
                ));
    }

    // 5. Cập nhật nhân viên
    public Employee updateEmployee(
            Long id,
            Employee employeeDetails) {

        // Kiểm tra nhân viên có tồn tại không
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay nhan vien co ID: " + id
                ));

        // Kiểm tra Department trước khi cập nhật
        validateDepartment(employeeDetails);

        // Kiểm tra email đã được sử dụng chưa
        boolean emailDaTonTai =
                employeeRepository.existsByEmail(
                        employeeDetails.getEmail()
                );

        // Kiểm tra email có phải email hiện tại của nhân viên không
        boolean emailKhongThayDoi =
                employee.getEmail() != null
                && employeeDetails.getEmail() != null
                && employee.getEmail().equalsIgnoreCase(
                        employeeDetails.getEmail()
                );

        // Không cho dùng email của nhân viên khác
        if (emailDaTonTai && !emailKhongThayDoi) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email nay da duoc su dung boi nhan vien khac"
            );
        }

        // Cập nhật thông tin nhân viên
        employee.setFullName(employeeDetails.getFullName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhone(employeeDetails.getPhone());
        employee.setPosition(employeeDetails.getPosition());
        employee.setDepartment(employeeDetails.getDepartment());

        return employeeRepository.save(employee);
    }

    // 6. Xóa nhân viên
    public String deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Khong tim thay nhan vien co ID: " + id
            );
        }

        employeeRepository.deleteById(id);

        return "Da xoa thanh cong nhan vien co ID: " + id;
    }

    // 7. Tìm kiếm theo tên hoặc email
    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword,
                        keyword
                );
    }

    // 8. Lọc nhân viên theo phòng ban
    public List<Employee> getEmployeesByDepartment(
            Long departmentId) {

        return employeeRepository.findByDepartment_Id(departmentId);
    }

    // 9. Lọc nhân viên theo chức vụ
    public List<Employee> getEmployeesByPosition(
            String position) {

        return employeeRepository
                .findByPositionContainingIgnoreCase(position);
    }

    // 10. Phân trang danh sách nhân viên
    public Page<Employee> getEmployeesPaginated(
            int page,
            int size) {

        // Kiểm tra tham số phân trang
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "So trang phai >= 0 va kich thuoc trang tu 1 den 100"
            );
        }

        // Tạo yêu cầu phân trang
        PageRequest pageRequest = PageRequest.of(page, size);

        // Lấy danh sách nhân viên theo trang
        return employeeRepository.findAll(pageRequest);
    }
}
