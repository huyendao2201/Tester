package com.rs.shopvn.services;

import com.rs.shopvn.dto.RegisterRequest;
import com.rs.shopvn.entity.ReferralCode;
import com.rs.shopvn.entity.User;
import com.rs.shopvn.repository.ReferralCodeRepository;
import com.rs.shopvn.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ReferralCodeRepository referralCodeRepository;

    private static final DateTimeFormatter DOB_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public UserService(UserRepository userRepository, ReferralCodeRepository referralCodeRepository) {
        this.userRepository = userRepository;
        this.referralCodeRepository = referralCodeRepository;
        seedReferralCodes();
    }

    private void seedReferralCodes() {
        // seed vài mã mẫu
        if (!referralCodeRepository.existsByCode("ABCD1234")) referralCodeRepository.save(new ReferralCode("ABCD1234"));
        if (!referralCodeRepository.existsByCode("SHOPVN01")) referralCodeRepository.save(new ReferralCode("SHOPVN01"));
        if (!referralCodeRepository.existsByCode("VN202600")) referralCodeRepository.save(new ReferralCode("VN202600"));
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean referralExists(String code) {
        return referralCodeRepository.existsByCode(code);
    }

    @Transactional
    public User register(RegisterRequest req) {
        User u = new User();
        u.setFullName(req.getFullName().trim());
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail().trim().toLowerCase());
        u.setPhone(req.getPhone());
        u.setPasswordHash(sha256(req.getPassword()));
        u.setGender(req.getGender());

        if (req.getDob() != null && !req.getDob().isBlank()) {
            LocalDate dob = LocalDate.parse(req.getDob(), DOB_FMT);
            u.setDob(dob);
        }
        if (req.getReferralCode() != null && !req.getReferralCode().isBlank()) {
            u.setReferralCode(req.getReferralCode());
        }
        return userRepository.save(u);
    }

    private String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : dig) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}