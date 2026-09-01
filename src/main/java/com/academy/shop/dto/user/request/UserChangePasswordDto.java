package com.academy.shop.dto.user.request;

import lombok.Data;

@Data
public class UserChangePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
