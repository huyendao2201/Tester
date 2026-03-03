package com.rs.shopvn.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "referral_codes")
public class ReferralCode {
    @Id
    @Column(length = 8)
    private String code;

    public ReferralCode() {}
    public ReferralCode(String code) { this.code = code; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}