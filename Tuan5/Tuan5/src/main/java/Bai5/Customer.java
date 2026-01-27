package Bai5;

import java.time.LocalDate;

public class Customer {
    public String customerId;     // Mã KH
    public String fullName;       // Họ tên
    public String email;
    public String phone;
    public String address;
    public String password;
    public String confirmPassword;
    public LocalDate dob;         // ngày sinh (nullable)
    public String gender;         // Nam/Nữ/Khác (nullable)
    public boolean acceptedTerms; // điều khoản
}
