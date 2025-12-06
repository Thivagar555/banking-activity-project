package com.banking.BankingProject.auth;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String email;
    private String otp;
    private String accountNumber;  // added
    private Double amount;
}
