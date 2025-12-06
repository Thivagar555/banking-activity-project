package com.banking.BankingProject.account;

import com.banking.BankingProject.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private double balance;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean locked = false;
    private int failedAttempts = 0;
    private LocalDateTime lockUntil;

}
