package com.swt.project.authService.repository;

import com.swt.project.authService.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    public Optional<Users> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("delete from Users u where u.email = ?1")
    void deleteByEmail(String email);


    boolean existsByEmail(String email);
}