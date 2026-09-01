package com.academy.shop.controller;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.dto.order.response.OrderPageDto;
import com.academy.shop.dto.user.response.UserPageDto;
import com.academy.shop.service.CartService;
import com.academy.shop.service.OrderService;
import com.academy.shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartServiceImpl;
    private final OrderService orderServiceImpl;
    private final UserService userServiceImpl;

    @GetMapping
    public String cart(HttpSession session, Model model) {
        model.addAttribute("cart", cartServiceImpl.getCart(session));

        return "cart";
    }

    @PostMapping("/add")
    public String add(
            @RequestParam Integer itemId,
            @RequestParam(defaultValue = "1") Integer amount,
            HttpSession session
    ) {
        cartServiceImpl.addItem(session, itemId, amount);

        return "redirect:/cart";
    }

    @PostMapping("/change")
    public String change(
            @RequestParam Integer itemId,
            @RequestParam Integer amount,
            HttpSession session
    ) {
        cartServiceImpl.changeAmount(session, itemId, amount);

        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Integer itemId, HttpSession session) {
        cartServiceImpl.removeItem(session, itemId);

        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(
            Authentication authentication,
            HttpSession session,
            Model model
    ) {
        UserPageDto userPageDto = userServiceImpl.findUserByUsername(authentication.getName());
        Integer userId = userPageDto.getId();
        CartDto cart = cartServiceImpl.getCart(session);

        OrderPageDto orderPageDto = orderServiceImpl.createOrder(cart, userId);
        cartServiceImpl.clear(session);

        return "redirect:/orders/" + orderPageDto.getOrderId();
    }

    @PostMapping("/clear")
    public String clear(HttpSession session) {
        cartServiceImpl.clear(session);

        return "redirect:/cart";
    }
}
