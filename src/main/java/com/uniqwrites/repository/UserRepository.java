package com.uniqwrites.repository;

import com.uniqwrites.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    boolean existsByGoogleId(String googleId);
    Optional<User> findByResetToken(String resetToken);
}
