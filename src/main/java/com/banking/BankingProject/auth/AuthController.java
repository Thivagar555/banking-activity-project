package com.banking.BankingProject.auth;

import com.banking.BankingProject.account.Account;
import com.banking.BankingProject.account.AccountRepository;
import com.banking.BankingProject.otp.OtpService;
import com.banking.BankingProject.security.JwtService;
import com.banking.BankingProject.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final JwtService jwtService;

    private String generateAccountNumber() {
        long timestamp = System.currentTimeMillis();
        return "91" + timestamp;
    }

    // -------------------------
    // USER REGISTRATION
    // -------------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .locked(false)
                .build();

        userRepository.save(user);

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(0.0)
                .user(user)
                .build();

        accountRepository.save(account);

        return ResponseEntity.ok("User registered successfully!");
    }

    // -------------------------
    // ADMIN REGISTRATION
    // -------------------------
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {

        User admin = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .locked(false)
                .build();

        userRepository.save(admin);

        return ResponseEntity.ok("Admin created successfully!");
    }

    // -------------------------
    // LOGIN → OTP
    // -------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // LOCK CHECK
        if (user.isLocked()
                && user.getLockUntil() != null
                && user.getLockUntil().isAfter(LocalDateTime.now())) {

            return ResponseEntity.status(423)
                    .body("Account is locked until: " + user.getLockUntil());
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        otpService.generateOtp(user.getEmail());
        return ResponseEntity.ok(Map.of("message", "OTP Sent"));
    }

    // -------------------------
    // VERIFY OTP → JWT COOKIE
    // -------------------------
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {

        if (!otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            return ResponseEntity.status(401).body("Invalid OTP");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT (should contain role)
        String jwt = jwtService.generateToken(user);

        // ✅ Correct cookie for Angular + withCredentials
        ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", jwt)
                .httpOnly(true)
                .secure(false) // true ONLY in HTTPS production
                .path("/")
                .sameSite("None") // IMPORTANT
                .maxAge(3600)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                        "email", user.getEmail(),
                        "role", user.getRole().name()
                ));
    }

    // -------------------------
    // LOGOUT → CLEAR COOKIE
    // -------------------------
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }
}
