package com.academy.shop;

import com.academy.shop.dto.user.request.UserChangePasswordDto;
import com.academy.shop.dto.user.request.UserCreateDto;
import com.academy.shop.model.entity.User;
import com.academy.shop.model.repository.UserRepository;
import com.academy.shop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void createUser_shouldEncodePassword() {
        UserCreateDto dto = UserCreateDto.builder()
                        .name("Иван")
                        .surname("Иванов")
                        .phoneNum("123456")
                        .username("ivan")
                        .password("password123")
                        .build();
        when(userRepository.findByUsername("ivan")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation ->
                        invocation.getArgument(0));
        userService.createUser(dto);
        verify(userRepository).save(argThat(user ->
                        user.getUsername().equals("ivan") && !user.getPassword()
                                .equals("password123") && passwordEncoder.matches(
                                        "password123",
                                user.getPassword()
                        )
                )
        );
    }

    @Test
    void createUser_whenUsernameExists_shouldThrowException() {
        User existingUser = new User();
        when(userRepository.findByUsername("ivan")).thenReturn(existingUser);
        UserCreateDto dto = UserCreateDto.builder()
                        .username("ivan")
                        .password("password")
                        .build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                        userService.createUser(dto));
        assertEquals("Пользователь с таким username уже существует", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void addMoney_shouldIncreaseBalance() {
        User user = new User();
        user.setId(1);
        user.setBalance(100F);
        when(userRepository.getReferenceById(1)).thenReturn(user);
        userService.addMoney(1, 500F);
        assertEquals(600F, user.getBalance());
        verify(userRepository).save(user);
    }

    @Test
    void addMoney_whenZero_shouldThrowException() {
        User user = new User();
        user.setBalance(100F);
        when(userRepository.getReferenceById(1)).thenReturn(user);
        assertThrows(IllegalArgumentException.class, () ->
                userService.addMoney(1, 0F));
    }

    @Test
    void addMoney_whenNegative_shouldThrowException() {
        User user = new User();
        user.setBalance(100F);
        when(userRepository.getReferenceById(1)).thenReturn(user);
        assertThrows(IllegalArgumentException.class, () ->
                userService.addMoney(1, -100F));
    }

    @Test
    void changePassword_shouldEncodeNewPassword() {
        User user = new User();
        user.setUsername("ivan");
        user.setPassword(passwordEncoder.encode("oldPassword"));
        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmNewPassword("newPassword");
        when(userRepository.findByUsername("ivan")).thenReturn(user);
        userService.changePassword("ivan", dto);
        assertTrue(passwordEncoder.matches(
                        "newPassword",
                        user.getPassword()
                )
        );

        assertFalse(user.getPassword().equals("newPassword"));
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_whenOldPasswordIncorrect_shouldThrowException() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("correct"));
        when(userRepository.findByUsername("ivan")).thenReturn(user);
        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setOldPassword("wrong");
        dto.setNewPassword("new");
        dto.setConfirmNewPassword("new");
        assertThrows(IllegalArgumentException.class, () ->
                userService.changePassword("ivan", dto)
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_whenConfirmationDoesNotMatch()
            throws Exception {
        User user = new User();
        user.setPassword(passwordEncoder.encode("old"));
        when(userRepository.findByUsername("ivan")).thenReturn(user);
        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setOldPassword("old");
        dto.setNewPassword("new");
        dto.setConfirmNewPassword("different");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                        userService.changePassword("ivan", dto));
        assertEquals("Новый пароль указан неверно", exception.getMessage());
    }

    @Test
    void changePassword_whenNewPasswordEqualsOld()
            throws Exception {
        User user = new User();
        user.setPassword(passwordEncoder.encode("password"));
        when(userRepository.findByUsername("ivan")).thenReturn(user);
        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setOldPassword("password");
        dto.setNewPassword("password");
        dto.setConfirmNewPassword("password");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                        userService.changePassword("ivan", dto));
        assertEquals(
                "Новый пароль должен отличаться от старого",
                exception.getMessage()
        );
    }

    @Test
    void blockUser_shouldLockAccount() {
        User user = new User();
        user.setAccountNonLocked(true);
        when(userRepository.getReferenceById(1)).thenReturn(user);
        userService.blockUser(1);
        assertFalse(user.getAccountNonLocked());
        verify(userRepository).save(user);
    }

    @Test
    void unblockUser_shouldUnlockAccount() {
        User user = new User();
        user.setAccountNonLocked(false);
        when(userRepository.getReferenceById(1)).thenReturn(user);
        userService.unblockUser(1);
        assertTrue(user.getAccountNonLocked());
    }

    @Test
    void disableUser_shouldDisableAccount() {
        User user = new User();
        user.setEnabled(true);
        when(userRepository.getReferenceById(1)).thenReturn(user);
        userService.disableUser(1);
        assertFalse(user.getEnabled());
    }

    @Test
    void loadUserByUsername_shouldReturnUser() {
        User user = new User();
        user.setUsername("ivan");
        when(userRepository.findByUsername("ivan")).thenReturn(user);
        User result = (User) userService.loadUserByUsername("ivan");
        assertSame(user, result);
    }

    @Test
    void loadUserByUsername_whenNotFound_shouldThrowException() {
        when(userRepository.findByUsername("ivan")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("ivan"));
    }
}
