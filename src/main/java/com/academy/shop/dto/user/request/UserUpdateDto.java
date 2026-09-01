package com.academy.shop.dto.user.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {
    private String name;
    private String surname;
    private String phoneNum;
    private String username;
}
