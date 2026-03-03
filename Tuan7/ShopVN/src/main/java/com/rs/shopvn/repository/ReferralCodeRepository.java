package com.rs.shopvn.repository;

import com.rs.shopvn.entity.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferralCodeRepository extends JpaRepository<ReferralCode, String> {
    boolean existsByCode(String code);
}