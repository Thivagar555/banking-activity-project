package com.banking.BankingProject.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// FIX: Implement the UserDetails interface
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean locked = false; // user freeze if fraud detected
    private LocalDateTime lockUntil;

    // -------------------------------------------------------------------
    //                       USERDETAILS IMPLEMENTATION
    // -------------------------------------------------------------------

    /**
     * Returns the authorities (roles) granted to the user.
     * This is the critical method called by the JwtAuthFilter.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We create a collection containing a single SimpleGrantedAuthority based on the Role enum name.
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the username used to authenticate the user.
     * In your application, this is the user's email.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns the password used to authenticate the user.
     * Spring Security expects the encoded password here.
     */
    @Override
    public String getPassword() {
        return password;
    }

    // The remaining methods below control account status.
    // They are set to return true (meaning "not expired/not locked/enabled")
    // unless you have specific logic implemented for these checks.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // This leverages your existing 'locked' field for security checks
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}