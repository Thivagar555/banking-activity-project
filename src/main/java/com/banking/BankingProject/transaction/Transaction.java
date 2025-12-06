package com.banking.BankingProject.transaction;

import com.banking.BankingProject.account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private String type; // DEPOSIT, WITHDRAW, TRANSFER

    private double amount;

    private LocalDateTime time = LocalDateTime.now();
}
