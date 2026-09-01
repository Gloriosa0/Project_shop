package com.academy.shop.model.repository;

import com.academy.shop.model.entity.Role;
import com.academy.shop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    List<User> findByEnabled(Boolean enabled);

    List<User> findByRolesContaining(Role role);

    List<User> findByAccountNonLocked(Boolean accountNonLocked);

    List<User> findByAccountNonLockedAndRolesContaining(Boolean accountNonLocked, Role role);

    List<User> findByEnabledAndRolesContaining(Boolean enabled, Role role);

    List<User> findByEnabledAndAccountNonLocked(Boolean enabled, Boolean accountNonLocked);

    List<User> findByRolesContainingAndAccountNonLockedAndEnabled(
            Role role,
            Boolean accountNonLocked,
            Boolean enabled);
}
