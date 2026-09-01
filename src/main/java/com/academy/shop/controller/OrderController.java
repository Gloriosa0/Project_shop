package com.academy.shop.controller;

import com.academy.shop.dto.order.response.OrderPageDto;
import com.academy.shop.dto.order.response.OrderShortDto;
import com.academy.shop.dto.user.response.UserPageDto;
import com.academy.shop.model.entity.Status;
import com.academy.shop.service.OrderService;
import com.academy.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String orders(Authentication authentication, Model model) {
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        List<OrderShortDto> orderShortDtos = orderService.findOrdersByUserId(userPageDto.getId());
        model.addAttribute("orderShortDtos", orderShortDtos);

        return "orderList";
    }

    @GetMapping("/{id}")
    public String order(@PathVariable Integer id, Authentication authentication, Model model) {
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        OrderPageDto orderPageDto = orderService.findOrderByIdAndUser(id, userPageDto.getId());
        model.addAttribute("orderPageDto", orderPageDto);

        return "order";
    }

    @GetMapping("/admin")
    public String ordersAdmin(Model model) {
        List<OrderShortDto> orderShortDtos = orderService.findAllOrders();
        model.addAttribute("orderShortDtos", orderShortDtos);

        return "orderList";
    }

    @GetMapping("/admin/{id}")
    public String orderAdmin(@PathVariable Integer id, Model model) {
        OrderPageDto orderPageDto = orderService.findOrderById(id);
        model.addAttribute("orderPageDto", orderPageDto);

        return "order";
    }

    @GetMapping("/search")
    public String search(
            Authentication authentication,
            @RequestParam(required = false)
            Status status,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime to,
            Model model
    ) {
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        List<OrderShortDto> orderShortDtos = orderService.findOrdersByParams(userPageDto.getId(), status, from, to);
        model.addAttribute("orderShortDtos", orderShortDtos);

        return "orderList";
    }

    @GetMapping("/admin/search")
    public String searchAdmin(
            @RequestParam(required = false)
            Integer userId,
            @RequestParam(required = false)
            Status status,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime to,
            Model model
    ) {
        List<OrderShortDto> orderShortDtos = orderService.findOrdersByParams(userId, status, from, to);
        model.addAttribute("orderShortDtos", orderShortDtos);

        return "orderList";
    }

    @PostMapping("/{id}/pay")
    public String pay(@PathVariable Integer id, Authentication authentication) {
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        orderService.pay(id, userPageDto.getId());

        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Integer id, Authentication authentication) {
        UserPageDto userPageDto = userService.findUserByUsername(authentication.getName());
        orderService.cancel(id, userPageDto.getId());

        return "redirect:/orders/" + id;
    }

    @PostMapping("/admin/{id}/status")
    public String changeStatus(
            @PathVariable Integer id,
            @RequestParam Status status
    ) {
        orderService.changeOrderStatus(id, status);

        return "redirect:/orders/admin/" + id;
    }

    @PostMapping("/admin/{id}/comment")
    public String changeComment(
            @PathVariable Integer id,
            @RequestParam String comment
    ){
        orderService.changeComment(id, comment);

        return "redirect:/orders/admin/" + id;
    }
}
