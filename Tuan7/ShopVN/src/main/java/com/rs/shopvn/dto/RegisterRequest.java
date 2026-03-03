package com.rs.shopvn.dto;

import com.rs.shopvn.validation.AgeRange;
import com.rs.shopvn.validation.PasswordMatches;
import jakarta.validation.constraints.*;

@PasswordMatches
public class RegisterRequest {

    // Họ và tên: chữ cái (có dấu/không) + khoảng trắng, 2-50
    // Dùng \p{L} cho mọi chữ cái Unicode
    @NotBlank(message = "Họ và tên là bắt buộc.")
    @Size(min = 2, max = 50, message = "Họ và tên phải từ 2–50 ký tự.")
    @Pattern(regexp = "^[\\p{L}]+(?:[\\p{L}\\s]*[\\p{L}]+)?$",
            message = "Họ và tên chỉ chứa chữ cái và dấu cách.")
    private String fullName;

    // username: chữ thường, số, _, bắt đầu bằng chữ cái, 5-20, unique (check ở service)
    @NotBlank(message = "Tên đăng nhập là bắt buộc.")
    @Size(min = 5, max = 20, message = "Tên đăng nhập phải từ 5–20 ký tự.")
    @Pattern(regexp = "^[a-z][a-z0-9_]{4,19}$",
            message = "Tên đăng nhập: chữ thường/số/_ và bắt đầu bằng chữ cái.")
    private String username;

    // Email: dùng @Email, và unique (check ở service)
    @NotBlank(message = "Email là bắt buộc.")
    @Email(message = "Email không đúng định dạng.")
    @Size(max = 254, message = "Email quá dài.")
    private String email;

    // SĐT VN: bắt đầu 0, 10 số liên tiếp
    @NotBlank(message = "Số điện thoại là bắt buộc.")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải là 10 chữ số và bắt đầu bằng 0.")
    private String phone;

    // Mật khẩu: 8–32, có hoa, thường, số, ký tự đặc biệt
    @NotBlank(message = "Mật khẩu là bắt buộc.")
    @Size(min = 8, max = 32, message = "Mật khẩu phải từ 8–32 ký tự.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,32}$",
            message = "Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số, 1 ký tự đặc biệt."
    )
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu là bắt buộc.")
    private String confirmPassword;

    // Ngày sinh: không bắt buộc, dd/mm/yyyy, tuổi 16 đến <100 (tính lúc đăng ký)
    // Client gửi dd/mm/yyyy; server sẽ validate format ở controller (parse) + AgeRange dựa trên string.
    @Pattern(regexp = "^$|^\\d{2}/\\d{2}/\\d{4}$", message = "Ngày sinh phải theo định dạng dd/mm/yyyy.")
    @AgeRange(min = 16, maxExclusive = 100, message = "Tuổi phải từ 16 đến dưới 100.")
    private String dob; // giữ dạng String "dd/MM/yyyy" cho dễ bind + validate

    // gender: optional
    private String gender; // MALE/FEMALE/NA

    // referral: optional, 8 ký tự hoa + số, tồn tại trong CSDL (check ở service)
    @Pattern(regexp = "^$|^[A-Z0-9]{8}$", message = "Mã giới thiệu phải gồm 8 ký tự chữ hoa và số.")
    private String referralCode;

    // Đồng ý điều khoản
    @AssertTrue(message = "Bạn phải đồng ý điều khoản.")
    private boolean agreeTerms;

    // getters/setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getReferralCode() { return referralCode; }
    public void setReferralCode(String referralCode) { this.referralCode = referralCode; }

    public boolean isAgreeTerms() { return agreeTerms; }
    public void setAgreeTerms(boolean agreeTerms) { this.agreeTerms = agreeTerms; }
}