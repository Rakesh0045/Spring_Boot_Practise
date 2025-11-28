package org.example.springjwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="password_reset_tokens",
        indexes = {
                    @Index(name="idx_token_hash", columnList="token_hash"),
                    @Index(name="idx_expires_at", columnList="expires_at")
                    })
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    private boolean used;
}
