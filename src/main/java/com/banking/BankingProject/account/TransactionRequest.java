package com.banking.BankingProject.account;

import lombok.Data;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {
    private String accountNumber;
    private double amount;

}
