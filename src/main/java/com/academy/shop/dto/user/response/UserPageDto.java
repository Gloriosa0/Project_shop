package com.academy.shop.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPageDto {
    private Integer id;
    private String name;
    private String surname;
    private String phoneNum;
    private String username;
    private Float balance;
    private Boolean accountNonLocked;
}
