package Bai5;

import java.sql.*;
import java.util.Optional;

public class CustomerDao {

    public boolean existsCustomerId(String customerId) throws SQLException {
        String sql = "SELECT 1 FROM customers WHERE customer_id = ? LIMIT 1";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM customers WHERE email = ? LIMIT 1";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insert(Customer cst, String passwordHash) throws SQLException {
        String sql = """
            INSERT INTO customers(customer_id, full_name, email, phone, address, password_hash, dob, gender)
            VALUES(?,?,?,?,?,?,?,?)
            """;

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cst.customerId);
            ps.setString(2, cst.fullName);
            ps.setString(3, cst.email);
            ps.setString(4, cst.phone);
            ps.setString(5, cst.address);
            ps.setString(6, passwordHash);

            if (cst.dob == null) ps.setNull(7, Types.DATE);
            else ps.setDate(7, Date.valueOf(cst.dob));

            if (cst.gender == null || cst.gender.isBlank()) ps.setNull(8, Types.VARCHAR);
            else ps.setString(8, cst.gender);

            ps.executeUpdate();
        }
    }
}
