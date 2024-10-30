package com.backend.spring.repositories;

import java.util.List;
import java.util.Optional;

import com.backend.spring.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "JOIN roles r ON ur.role_id = r.role_id WHERE r.role_name = ?1 " +
            "AND (u.address LIKE CONCAT('%', ?2, '%') OR u.email LIKE CONCAT('%', ?2, '%') " +
            "OR u.full_name LIKE CONCAT('%', ?2, '%') OR u.phone_number LIKE CONCAT('%', ?2, '%')) AND u.deleted_at IS NULL", nativeQuery = true)
    Page<User> getAllLearners(String roleName, String keyword, Pageable pageable);

    @Query(value = "SELECT COUNT(ur.user_id) FROM user_roles ur WHERE ur.role_id = ?1", nativeQuery = true)
    Long countByRoles(Integer valueRole);

    Optional<User> findByEmail(String email);
}
