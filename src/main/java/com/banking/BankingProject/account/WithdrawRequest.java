package com.banking.BankingProject.account;

import lombok.Data;

@Data
public class WithdrawRequest {
    private String accountNumber;
    private double amount;
}
