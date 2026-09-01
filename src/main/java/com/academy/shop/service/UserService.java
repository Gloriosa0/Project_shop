package com.academy.shop.service;

import com.academy.shop.dto.user.request.UserChangePasswordDto;
import com.academy.shop.dto.user.request.UserCreateDto;
import com.academy.shop.dto.user.request.UserUpdateDto;
import com.academy.shop.dto.user.response.UserListForAdminDto;
import com.academy.shop.dto.user.response.UserPageDto;
import com.academy.shop.model.entity.Role;

import java.util.List;

public interface UserService {
    void createUser(UserCreateDto userCreateDto);

    UserPageDto findUser(Integer id);

    UserPageDto findUserByUsername(String username);

    List<UserListForAdminDto> findAllUsers();

    List<UserListForAdminDto> findUsersByParams(Role role, Boolean enabled, Boolean accountNonLocked);

    UserPageDto updateUser(Integer id, UserUpdateDto userUpdateDto);

    void addMoney(Integer userId, Float money);

    void changePassword(String userName, UserChangePasswordDto userChangePasswordDto);

    void blockUser(Integer id);

    void unblockUser(Integer id);

    void disableUser(Integer id);
}
