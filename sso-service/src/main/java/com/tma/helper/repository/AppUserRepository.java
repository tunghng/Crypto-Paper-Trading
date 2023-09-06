package com.tma.helper.repository;

import com.tma.helper.model.AppUser;
import com.tma.helper.model.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    Page<AppUser> findAllByRole(RoleType role, Pageable pageable);
}
