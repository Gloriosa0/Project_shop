package com.academy.shop.dto.user.response;

import com.academy.shop.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class UserListForAdminDto {
    private Integer id;
    private String name;
    private String surname;
    private String phoneNum;
    private String username;
    private Float balance;
    private Boolean enabled;
    private Boolean accountNonLocked;
    public List<Role> roles;
}
