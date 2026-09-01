package com.academy.shop;

import com.academy.shop.model.entity.Role;
import com.academy.shop.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getAuthorities_shouldReturnUserRoles() {
        User user = new User();
        user.setRoles(List.of(Role.ROLE_USER, Role.ROLE_ADMIN));

        var authorities = user.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
                        .contains("ROLE_USER")
        );

        assertTrue(authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
                        .contains("ROLE_ADMIN")
        );
    }

    @Test
    void isEnabled_shouldReturnCorrectValue() {
        User user = new User();
        user.setEnabled(true);
        assertTrue(user.isEnabled());
        user.setEnabled(false);
        assertFalse(user.isEnabled());
        user.setEnabled(null);
        assertFalse(user.isEnabled());
    }

    @Test
    void isAccountNonLocked_shouldReturnCorrectValue() {
        User user = new User();
        user.setAccountNonLocked(true);
        assertTrue(user.isAccountNonLocked());
        user.setAccountNonLocked(false);
        assertFalse(user.isAccountNonLocked());
    }

    @Test
    void isAccountNonExpired_shouldReturnCorrectValue() {
        User user = new User();
        user.setAccountNonExpired(true);
        assertTrue(user.isAccountNonExpired());
        user.setAccountNonExpired(false);
        assertFalse(user.isAccountNonExpired());
    }

    @Test
    void isCredentialsNonExpired_shouldReturnCorrectValue() {
        User user = new User();
        user.setCredentialsNonExpired(true);
        assertTrue(user.isCredentialsNonExpired());
        user.setCredentialsNonExpired(false);
        assertFalse(user.isCredentialsNonExpired());
    }
}
