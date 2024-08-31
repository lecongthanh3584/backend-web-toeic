package com.backend.spring.repository;

import java.util.List;
import java.util.Optional;

import com.backend.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT u.* FROM users u " +
            "JOIN user_roles ur ON u.user_id = ur.user_id " +
            "JOIN roles r ON ur.role_id = r.role_id WHERE r.role_name = ?1", nativeQuery = true)
    List<User> findByRoles(String roleName);

    @Query(value = "SELECT COUNT(ur.user_id) FROM user_roles ur WHERE ur.role_id = ?1", nativeQuery = true)
    Long countByRoles(Integer valueRole);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByVerificationCode(String verificationCode);

    User findByEmail(String to);
}
