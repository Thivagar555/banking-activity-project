package com.banking.BankingProject.account;

import lombok.Data;

@Data
public class TransferRequest {
    private String receiverAccountNumber;
    private double amount;
}
