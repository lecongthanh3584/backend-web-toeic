package com.backend.spring.repositories;

import java.util.Optional;

import com.backend.spring.enums.ERole;
import com.backend.spring.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(ERole name);
}
