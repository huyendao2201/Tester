package Bai5;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerValidator {

    // 1) Mã KH: 6-10, chỉ chữ/số
    private static final Pattern CUSTOMER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,10}$");

    // 3) Email format (đủ dùng cho bài lab)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // 4) Phone: bắt đầu 0, tổng 10-12 số => 0 + (9..11 số)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9,11}$");

    private final CustomerDao dao;

    public CustomerValidator(CustomerDao dao) {
        this.dao = dao;
    }

    public List<String> validate(Customer c) {
        List<String> errors = new ArrayList<>();

        // 1) Mã KH
        if (isBlank(c.customerId)) {
            errors.add("Mã khách hàng là bắt buộc.");
        } else {
            String id = c.customerId.trim();
            if (!CUSTOMER_ID_PATTERN.matcher(id).matches()) {
                errors.add("Mã khách hàng phải dài 6 đến 10 ký tự và chỉ gồm chữ cái (a-z, A-Z) và số (0-9).");
            } else {
                try {
                    if (dao.existsCustomerId(id)) {
                        errors.add("Mã khách hàng phải là duy nhất (không được trùng lặp).");
                    }
                } catch (SQLException e) {
                    errors.add("Lỗi CSDL khi kiểm tra trùng Mã khách hàng: " + e.getMessage());
                }
            }
        }

        // 2) Họ và Tên
        if (isBlank(c.fullName)) {
            errors.add("Họ và Tên là bắt buộc.");
        } else {
            String name = c.fullName.trim();
            if (name.length() < 5 || name.length() > 50) {
                errors.add("Họ và Tên phải dài từ 5 đến 50 ký tự.");
            }
            // Cho phép tiếng Việt có dấu và khoảng trắng => không chặn regex (tránh chặn nhầm)
        }

        // 3) Email
        if (isBlank(c.email)) {
            errors.add("Email là bắt buộc.");
        } else {
            String email = c.email.trim();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                errors.add("Email phải có định dạng hợp lệ (ví dụ: nguyenvana@email.com).");
            } else {
                try {
                    if (dao.existsEmail(email)) {
                        errors.add("Email không được trùng lặp.");
                    }
                } catch (SQLException e) {
                    errors.add("Lỗi CSDL khi kiểm tra trùng Email: " + e.getMessage());
                }
            }
        }

        // 4) SĐT
        if (isBlank(c.phone)) {
            errors.add("Số điện thoại là bắt buộc.");
        } else {
            String phone = c.phone.trim();
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                errors.add("Số điện thoại chỉ gồm số (0-9), dài từ 10 đến 12 ký tự và phải bắt đầu bằng số 0.");
            }
        }

        // 5) Địa chỉ
        if (isBlank(c.address)) {
            errors.add("Địa chỉ là bắt buộc.");
        } else if (c.address.trim().length() > 255) {
            errors.add("Địa chỉ có độ dài tối đa 255 ký tự.");
        }

        // 6) Mật khẩu
        if (isBlank(c.password)) {
            errors.add("Mật khẩu là bắt buộc.");
        } else if (c.password.length() < 8) {
            errors.add("Mật khẩu phải có độ dài tối thiểu 8 ký tự.");
        }

        // 7) Xác nhận MK
        if (isBlank(c.confirmPassword)) {
            errors.add("Xác nhận Mật khẩu là bắt buộc.");
        } else if (c.password != null && !c.confirmPassword.equals(c.password)) {
            errors.add("Xác nhận Mật khẩu phải khớp chính xác với trường Mật khẩu.");
        }

        // 8) Ngày sinh (không bắt buộc) - nếu nhập phải đủ 18
        if (c.dob != null) {
            int years = Period.between(c.dob, LocalDate.now()).getYears();
            if (years < 18) {
                errors.add("Nếu nhập ngày sinh, người dùng phải đủ 18 tuổi (tính đến ngày hiện tại).");
            }
        }

        // 10) Điều khoản
        if (!c.acceptedTerms) {
            errors.add("Điều khoản dịch vụ: bắt buộc phải được tích chọn.");
        }

        return errors;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
