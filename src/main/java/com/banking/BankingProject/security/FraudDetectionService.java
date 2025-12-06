package com.banking.BankingProject.security;

import com.banking.BankingProject.account.Account;
import com.banking.BankingProject.email.EmailService;
import com.banking.BankingProject.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final TransactionRepository transactionRepository;
    private final EmailService emailService;

    public String analyze(Account sender, double amount) {

        // Rule 1 — Account Locked
        if(sender.isLocked()) {
            return "❌ Your account is currently locked due to suspicious activity.";
        }

        // Rule 2 — Large amount trigger
        if(amount > 50000) {
            return "⚠️ High amount flagged. Additional verification required.";
        }

        // Rule 3 — Rapid transaction behavior
        LocalDateTime twoMinAgo = LocalDateTime.now().minusMinutes(2);

        long recentTransfers = transactionRepository.countRecentTransfers(sender.getId(), twoMinAgo);

        if(recentTransfers > 3) {
            sender.setLocked(true);
            sender.setLockUntil(LocalDateTime.now().plusMinutes(30));
            return "🚨 Too many transfers in short time. Account temporarily locked.";
        }
        emailService.sendEmail(sender.getUser().getEmail(),
                "⚠ Suspicious Activity",
                "A potentially risky login or transfer was detected.");


        return "OK";
    }
}
