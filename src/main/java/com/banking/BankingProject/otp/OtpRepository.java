package com.banking.BankingProject.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    @Query("SELECT o FROM OtpVerification o WHERE o.email = :email AND o.otp = :otp AND o.used = false AND o.expiry > :time")
    Optional<OtpVerification> findValidOtp(String email, String otp, LocalDateTime time);
}
