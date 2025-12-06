package com.banking.BankingProject.admin;

import com.banking.BankingProject.account.Account;
import com.banking.BankingProject.account.AccountRepository;
import com.banking.BankingProject.user.User;
import com.banking.BankingProject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    // 1️⃣ Get all locked users (MODIFIED)
    @GetMapping("/locked-users")
    public ResponseEntity<?> getLockedUsers(){
        List<User> lockedUsers = userRepository.findByLockedTrue();

        if (lockedUsers.isEmpty()) {
            // Return a professional-looking message with HTTP 200 OK status
            return ResponseEntity.ok("✅ All user accounts are currently unlocked. No locked accounts found.");
        }

        return ResponseEntity.ok(lockedUsers);
    }

    // 2️⃣ Unlock user account
    @PutMapping("/unlock/{id}")
    public ResponseEntity<String> unlockUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow();
        user.setLocked(false);
        user.setLockUntil(null);
        userRepository.save(user);

        return ResponseEntity.ok("🔓 User unlocked successfully");
    }

    // 3️⃣ All accounts list
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> allAccounts(){
        return ResponseEntity.ok(accountRepository.findAll());
    }
}