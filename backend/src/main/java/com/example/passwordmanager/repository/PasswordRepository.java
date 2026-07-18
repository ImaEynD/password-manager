package com.example.passwordmanager.repository;

import com.example.passwordmanager.model.PasswordEntity;
import com.example.passwordmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordEntity, Long> {

    List<PasswordEntity> findByUser(User user);

    Optional<PasswordEntity> findByUserAndService(User user, String service);

    boolean existsByUserAndService(User user, String service);

    @Transactional
    void deleteByUserAndService(User user, String service);

    @Transactional
    void deleteByUser(User user);
}