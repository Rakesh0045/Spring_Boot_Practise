package org.example.springjwt.repository;

import org.example.springjwt.entity.PasswordResetToken;
import org.example.springjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    List<PasswordResetToken> findAllByUserAndUsedFalseAndExpiresAtAfter(User user, LocalDateTime now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiresAt < :now")
    void deleteAllExpiredSince(LocalDateTime now);


}