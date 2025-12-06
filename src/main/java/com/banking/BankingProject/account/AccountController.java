package com.banking.BankingProject.account;

import com.banking.BankingProject.auth.OtpVerifyRequest;
import com.banking.BankingProject.email.EmailService;
import com.banking.BankingProject.otp.OtpService;
import com.banking.BankingProject.security.FraudDetectionService;
import com.banking.BankingProject.security.JwtService;
import com.banking.BankingProject.transaction.Transaction;
import com.banking.BankingProject.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final TransactionRepository transactionRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final FraudDetectionService fraudDetectionService;

    // -------------------------
    // DEPOSIT (ADMIN / INTERNAL)
    // -------------------------
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request){

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + request.getAmount());

        transactionRepository.save(
                Transaction.builder()
                        .account(account)
                        .type("DEPOSIT")
                        .amount(request.getAmount())
                        .build()
        );

        return ResponseEntity.ok("Deposit successful");
    }

    // -------------------------
    // MY ACCOUNT (COOKIE JWT)
    // -------------------------
    @GetMapping("/my-account")
    public ResponseEntity<?> getMyAccount(
            @CookieValue("AUTH_TOKEN") String token) {

        String email = jwtService.extractEmail(token);

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return ResponseEntity.ok(account);
    }

    // -------------------------
    // WITHDRAW
    // -------------------------
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestBody WithdrawRequest request,
            @CookieValue("AUTH_TOKEN") String token) {

        String email = jwtService.extractEmail(token);

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getAccountNumber().equals(request.getAccountNumber())) {
            return ResponseEntity.status(403).body("Unauthorized account access");
        }

        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero");
        }

        if (account.getBalance() < request.getAmount()) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        }

        account.setBalance(account.getBalance() - request.getAmount());
        accountRepository.save(account);

        transactionRepository.save(
                Transaction.builder()
                        .account(account)
                        .type("WITHDRAW")
                        .amount(request.getAmount())
                        .build()
        );

        return ResponseEntity.ok("Withdraw successful");
    }

    // -------------------------
    // TRANSACTION HISTORY
    // -------------------------
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(
            @CookieValue("AUTH_TOKEN") String token) {

        String email = jwtService.extractEmail(token);

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return ResponseEntity.ok(
                transactionRepository.findByAccountId(account.getId())
        );
    }

    // -------------------------
    // TRANSFER → REQUEST OTP
    // -------------------------
    @PostMapping("/transfer/request")
    public ResponseEntity<?> requestTransfer(
            @RequestBody TransferRequest request,
            @CookieValue("AUTH_TOKEN") String token) {

        String email = jwtService.extractEmail(token);
        Account sender = accountRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        if (sender.getAccountNumber().equals(request.getReceiverAccountNumber())) {
            return ResponseEntity.badRequest().body("❌ Cannot transfer to same account");
        }

        Optional<Account> receiverOpt =
                accountRepository.findByAccountNumber(request.getReceiverAccountNumber());

        if (receiverOpt.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Receiver not found");
        }

        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body("❌ Invalid amount");
        }

        if (sender.getBalance() < request.getAmount()) {
            return ResponseEntity.badRequest().body("❌ Insufficient balance");
        }

        String fraudResult = fraudDetectionService.analyze(sender, request.getAmount());
        if (!fraudResult.equals("OK")) {
            return ResponseEntity.status(403).body(fraudResult);
        }

        if (request.getAmount() > 50_000) {
            return ResponseEntity.badRequest()
                    .body("🚨 Transfer blocked: amount exceeds limit");
        }

        otpService.generateOtp(email);
        return ResponseEntity.ok("OTP sent to confirm transfer");
    }

    // -------------------------
    // TRANSFER → CONFIRM OTP
    // -------------------------
    @PostMapping("/transfer/confirm")
    public ResponseEntity<?> confirmTransfer(
            @RequestBody OtpVerifyRequest request,
            @CookieValue("AUTH_TOKEN") String token) {

        String email = jwtService.extractEmail(token);

        if (!otpService.verifyOtp(email, request.getOtp())) {
            return ResponseEntity.status(401).body("❌ Invalid OTP");
        }

        Account sender = accountRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Account receiver = accountRepository
                .findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        double amount = request.getAmount();

        if (sender.getBalance() < amount) {
            return ResponseEntity.badRequest().body("❌ Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        transactionRepository.save(
                Transaction.builder()
                        .account(sender)
                        .type("DEBIT - Transfer to " + receiver.getAccountNumber())
                        .amount(amount)
                        .build());

        transactionRepository.save(
                Transaction.builder()
                        .account(receiver)
                        .type("CREDIT - Received from " + sender.getAccountNumber())
                        .amount(amount)
                        .build());

        emailService.sendEmail(
                email,
                "Transfer Successful",
                "₹" + amount + " sent to " + receiver.getAccountNumber()
        );

        return ResponseEntity.ok("✅ Transfer successful");
    }
}
