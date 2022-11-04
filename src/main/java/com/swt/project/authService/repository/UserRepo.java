package com.swt.project.authService.repository;

import com.swt.project.authService.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    public Optional<Users> findByEmail(String email);
}