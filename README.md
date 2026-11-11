# HỆ THỐNG QUẢN LÝ NHÂN VIÊN (EMPLOYEE MANAGEMENT SYSTEM)

## 1. Giới thiệu

Employee Management System là dự án xây dựng hệ thống quản lý nhân sự cho doanh nghiệp, giúp quản trị viên quản lý thông tin nhân viên, phòng ban, tài khoản và theo dõi hoạt động chấm công.

Dự án được phát triển theo mô hình RESTful API, sử dụng Spring Boot kết hợp MySQL để xử lý nghiệp vụ và lưu trữ dữ liệu.

## 2. Công nghệ sử dụng

- **Ngôn ngữ:** Java
- **Backend Framework:** Spring Boot 3
- **Database:** MySQL
- **ORM:** Spring Data JPA, Hibernate
- **Bảo mật:** Spring Security, BCrypt
- **Validation:** Jakarta Bean Validation
- **Build Tool:** Maven
- **API Testing:** Postman

## 3. Các chức năng chính

### Quản lý nhân viên
- Thêm, cập nhật và xem thông tin nhân viên.
- Tìm kiếm nhân viên theo tên.
- Lọc nhân viên theo phòng ban và chức vụ.
- Phân trang danh sách nhân viên.
- Quản lý trạng thái hoạt động của nhân viên.
- Kiểm tra dữ liệu đầu vào và email trùng lặp.

### Quản lý phòng ban
- Thêm, cập nhật và xóa phòng ban.
- Liên kết nhân viên với phòng ban.
- Xem danh sách nhân viên thuộc từng phòng ban.

### Quản lý tài khoản và phân quyền
- Đăng nhập bằng tài khoản và mật khẩu.
- Mã hóa mật khẩu bằng BCrypt.
- Phân quyền ADMIN và EMPLOYEE.
- ADMIN có quyền quản lý hệ thống.
- EMPLOYEE chỉ được truy cập thông tin cá nhân và chức năng dành cho nhân viên.

### Quản lý chấm công
- Ghi nhận thời gian vào và ra.
- Kiểm tra trạng thái đi làm đúng giờ hoặc đi muộn.
- Tính thời gian làm việc và thời gian đi muộn.
- Theo dõi lịch sử chấm công.
- Tự động ghi nhận vắng mặt đối với nhân viên chưa chấm công vào cuối ngày làm việc.

### Chấm công bằng mã QR
- Tạo mã QR phục vụ chấm công.
- Nhân viên quét mã QR để xác nhận chấm công.
- Kiểm tra phiên chấm công và thời hạn mã QR.

### Dashboard
- Tổng số nhân viên.
- Số nhân viên đang hoạt động.
- Số nhân viên đi muộn trong ngày.
- Số nhân viên vắng mặt trong ngày.

## 4. Kiến trúc dự án

Dự án được tổ chức theo mô hình phân lớp:

- **Controller:** Tiếp nhận HTTP Request và trả về HTTP Response.
- **Service:** Xử lý nghiệp vụ của hệ thống.
- **Repository:** Giao tiếp với cơ sở dữ liệu thông qua Spring Data JPA.
- **Entity:** Ánh xạ các đối tượng với bảng trong MySQL.
- **DTO:** Trao đổi dữ liệu giữa Client và Server.
- **Config:** Cấu hình bảo mật và khởi tạo dữ liệu.
- **Exception:** Xử lý lỗi tập trung.

## 5. Cài đặt và chạy dự án

### Yêu cầu môi trường

- JDK 17 trở lên.
- MySQL 8 trở lên.
- Maven.
- IDE: IntelliJ IDEA hoặc Eclipse.

### Bước 1: Clone dự án

```bash
git clone https://github.com/nguyentuanvu-26/employee-management-backend.git
```

### Bước 2: Tạo cơ sở dữ liệu

Tạo database trong MySQL:

```sql
CREATE DATABASE employee_management;
```

### Bước 3: Cấu hình kết nối

Sao chép file `application-example.properties` thành `application.properties` trong thư mục:

`src/main/resources/`

Cập nhật thông tin kết nối MySQL và tài khoản quản trị viên trong file cấu hình.

**Lưu ý:** Không sử dụng mật khẩu mẫu trong môi trường thực tế.

### Bước 4: Chạy ứng dụng

Mở Terminal tại thư mục dự án và chạy:

```bash
mvn spring-boot:run
```

Backend mặc định chạy tại:

`http://localhost:8080`

## 6. Kiểm thử API

Sử dụng Postman để kiểm thử các API của hệ thống.

Một số nhóm API chính:

- `/api/employees`
- `/api/departments`
- `/api/users`
- `/api/attendance`
- `/api/qr-attendance`
- `/api/dashboard`
- `/api/me`

Các API được bảo vệ bằng Spring Security và phân quyền theo vai trò người dùng.

## 7. Hướng phát triển

- Bổ sung Unit Test và Integration Test.
- Hoàn thiện xử lý lỗi và ghi log.
- Nâng cao bảo mật và quản lý phiên đăng nhập.
- Triển khai ứng dụng lên môi trường thực tế.

## 8. Tác giả

**Nguyễn Tuấn Vũ**

Dự án cá nhân phục vụ quá trình học tập và thực hành phát triển ứng dụng Java Backend với Spring Boot.