package com.academy.shop.controller;

import com.academy.shop.dto.user.request.UserChangePasswordDto;
import com.academy.shop.dto.user.request.UserCreateDto;
import com.academy.shop.dto.user.request.UserUpdateDto;
import com.academy.shop.dto.user.response.UserListForAdminDto;
import com.academy.shop.dto.user.response.UserPageDto;
import com.academy.shop.model.entity.Role;
import com.academy.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@RequestParam String name,
                               @RequestParam String surname,
                               @RequestParam String phoneNum,
                               @RequestParam String username,
                               @RequestParam String password) {
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name(name)
                .surname(surname)
                .phoneNum(phoneNum)
                .username(username)
                .password(password)
                .build();

        userService.createUser(userCreateDto);

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String user(Model model, Authentication authentication) {
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        model.addAttribute("userPageDto", userPageDto);
        model.addAttribute("isOwnProfile", true);
        return "profile";
    }

    @GetMapping("/profile/change")
    public String changeProfile(Model model, Authentication authentication) {
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name(userPageDto.getName())
                .surname(userPageDto.getSurname())
                .phoneNum(userPageDto.getPhoneNum())
                .username(userPageDto.getUsername())
                .build();
        model.addAttribute("userUpdateDto", userUpdateDto);

        return "changeProfile";
    }

    @PostMapping("/profile/change")
    public String changeProfile(
            Authentication authentication,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String phoneNum,
            @RequestParam String username){
        Integer userId = userService.findUserByUsername(authentication.getName()).getId();
        String oldUsername = userService.findUserByUsername(authentication.getName()).getUsername();
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name(name)
                .surname(surname)
                .phoneNum(phoneNum)
                .username(username)
                .build();
        UserPageDto userPageDto = userService.updateUser(userId, userUpdateDto);
        if (!userPageDto.getUsername().equals(oldUsername)) {
            return "redirect:/logout";
        }

        return "redirect:/profile";
    }

    @GetMapping("/profile/password")
    public String changePassword(){
        return "changePassword";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @ModelAttribute UserChangePasswordDto userChangePasswordDto,
            Authentication authentication
    ){
        userService.changePassword(authentication.getName(), userChangePasswordDto);
        return "redirect:/profile";
    }

    @PostMapping("/profile/add-money")
    public String addMoney(
            Authentication authentication,
            @RequestParam Float money){
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        userService.addMoney(userPageDto.getId(), money);

        return "redirect:/profile";
    }

    @PostMapping("/profile/disable")
    public String disable(Authentication authentication){
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        userService.disableUser(userPageDto.getId());

        return "redirect:/logout";
    }

    @GetMapping("/admin/users/{userId}")
    public String userAdmin(Model model,
                            @PathVariable Integer userId,
                            Authentication authentication
    ) {
        UserPageDto userPageDto = userService.findUser(userId);
        boolean isOwnProfile = userPageDto.getUsername().equals(authentication.getName());
        model.addAttribute("userPageDto", userPageDto);
        model.addAttribute("isOwnProfile", isOwnProfile);

        return "profile";
    }

    @GetMapping("/admin/users")
    public String usersAllAdmin(Model model){
        List<UserListForAdminDto> userListForAdminDtos = userService.findAllUsers();
        model.addAttribute("userListForAdminDtos", userListForAdminDtos);
        model.addAttribute("roles", Role.values());

        return "users";
    }

    @GetMapping("/admin/users/search")
    public String usersAdmin(
            Model model,
            @RequestParam(required = false)
            Role role,
            @RequestParam(required = false)
            Boolean enabled,
            @RequestParam(required = false)
            Boolean accountNonLocked
    ) {
        List<UserListForAdminDto> userListForAdminDtos = userService.findUsersByParams(
                role,
                enabled,
                accountNonLocked);
        model.addAttribute("userListForAdminDtos", userListForAdminDtos);
        model.addAttribute("roles", Role.values());

        return "users";
    }

    @PostMapping("/admin/users/{userId}/blacklist")
    public String blacklistAdmin(@PathVariable Integer userId) {
        userService.blockUser(userId);

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/{userId}/unblacklist")
    public String unblacklistAdmin(@PathVariable Integer userId) {
        userService.unblockUser(userId);

        return "redirect:/admin/users";
    }
}

