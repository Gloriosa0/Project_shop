package com.academy.shop.service.impl;

import com.academy.shop.dto.user.request.UserChangePasswordDto;
import com.academy.shop.dto.user.request.UserCreateDto;
import com.academy.shop.dto.user.request.UserUpdateDto;
import com.academy.shop.dto.user.response.UserListForAdminDto;
import com.academy.shop.dto.user.response.UserPageDto;
import com.academy.shop.model.entity.Role;
import com.academy.shop.model.entity.User;
import com.academy.shop.model.repository.UserRepository;
import com.academy.shop.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void createUser(UserCreateDto userCreateDto) {
        if (userRepository.findByUsername(userCreateDto.getUsername()) != null) {
            throw new IllegalArgumentException(
                    "Пользователь с таким username уже существует"
            );
        }
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setSurname(userCreateDto.getSurname());
        user.setPhoneNum(userCreateDto.getPhoneNum());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setBalance(0F);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRoles(List.of(Role.ROLE_USER));

        userRepository.save(user);
    }

    @Override
    public UserPageDto findUser(Integer id) {
        User user = userRepository.getReferenceById(id);
        return UserPageDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNum(user.getPhoneNum())
                .username(user.getUsername())
                .balance(user.getBalance())
                .accountNonLocked(user.isAccountNonLocked())
                .build();
    }

    @Override
    public UserPageDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return UserPageDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNum(user.getPhoneNum())
                .username(user.getUsername())
                .balance(user.getBalance())
                .accountNonLocked(user.isAccountNonLocked())
                .build();
    }

    @Override
    public List<UserListForAdminDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserListForAdminDto> userListForAdminDtos = new ArrayList<>();
        for (User user : users) {
            userListForAdminDtos.add(
                    UserListForAdminDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .phoneNum(user.getPhoneNum())
                            .username(user.getUsername())
                            .balance(user.getBalance())
                            .enabled(user.isEnabled())
                            .accountNonLocked(user.isAccountNonLocked())
                            .roles(user.getRoles())
                            .build());
        }
        return userListForAdminDtos;
    }

    @Override
    public List<UserListForAdminDto> findUsersByParams(Role role, Boolean enabled, Boolean accountNonLocked) {
        List<User> users = new ArrayList<>();
        if (role != null && accountNonLocked != null && enabled != null) {
            users.addAll(userRepository.findByRolesContainingAndAccountNonLockedAndEnabled(
                    role,
                    accountNonLocked,
                    enabled
            ));
        } else if (role != null && accountNonLocked != null) {
            users.addAll(userRepository.findByAccountNonLockedAndRolesContaining(
                    accountNonLocked,
                    role
            ));
        } else if (enabled != null && role != null) {
            users.addAll(userRepository.findByEnabledAndRolesContaining(
                    enabled,
                    role
            ));
        } else if (enabled != null && accountNonLocked != null) {
            users.addAll(userRepository.findByEnabledAndAccountNonLocked(
                    enabled,
                    accountNonLocked
            ));
        } else if (role != null) {
            users.addAll(userRepository.findByRolesContaining(role));
        } else if (accountNonLocked != null) {
            users.addAll(userRepository.findByAccountNonLocked(accountNonLocked));
        } else if (enabled != null) {
            users.addAll(userRepository.findByEnabled(enabled));
        } else {
            users.addAll(userRepository.findAll());
        }
        List<UserListForAdminDto> userListForAdminDtos = new ArrayList<>();
        for (User user : users) {
            userListForAdminDtos.add(
                    UserListForAdminDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .phoneNum(user.getPhoneNum())
                            .username(user.getUsername())
                            .balance(user.getBalance())
                            .enabled(user.isEnabled())
                            .accountNonLocked(user.isAccountNonLocked())
                            .roles(user.getRoles())
                            .build());
        }
        return userListForAdminDtos;
    }

    @Transactional
    @Override
    public UserPageDto updateUser(Integer id, UserUpdateDto userUpdateDto) {
        User user = userRepository.getReferenceById(id);
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getSurname() != null) {
            user.setSurname(userUpdateDto.getSurname());
        }
        if (userUpdateDto.getPhoneNum() != null) {
            user.setPhoneNum(userUpdateDto.getPhoneNum());
        }
        if (userUpdateDto.getUsername() != null &&
                userRepository.findByUsername(userUpdateDto.getUsername()) == null) {
            user.setUsername(userUpdateDto.getUsername());
        }

        userRepository.save(user);

        User newUser = userRepository.getReferenceById(id);

        return UserPageDto.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .surname(newUser.getSurname())
                .phoneNum(newUser.getPhoneNum())
                .username(newUser.getUsername())
                .balance(newUser.getBalance())
                .accountNonLocked(newUser.isAccountNonLocked())
                .build();
    }

    @Transactional
    @Override
    public void addMoney(Integer userId, Float money) {
        User user = userRepository.getReferenceById(userId);
        if (money != null && money > 0) {
            user.setBalance(user.getBalance() + money);
        } else {
            throw new IllegalArgumentException(
                    "Невозможно пополнить счет негативной или нулевой суммой"
            );
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void changePassword(String userName, UserChangePasswordDto userChangePasswordDto) {
        User user = userRepository.findByUsername(userName);
        if (!passwordEncoder.matches(userChangePasswordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Старый пароль указан неверно"
            );
        }
        if (!userChangePasswordDto.getNewPassword().equals(userChangePasswordDto.getConfirmNewPassword())) {
            throw new IllegalArgumentException(
                    "Новый пароль указан неверно"
            );
        }
        if (passwordEncoder.matches(userChangePasswordDto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Новый пароль должен отличаться от старого"
            );
        }
        user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void blockUser(Integer id) {
        User user = userRepository.getReferenceById(id);
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void unblockUser(Integer id) {
        User user = userRepository.getReferenceById(id);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

    @Override
    public void disableUser(Integer id) {
        User user = userRepository.getReferenceById(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
