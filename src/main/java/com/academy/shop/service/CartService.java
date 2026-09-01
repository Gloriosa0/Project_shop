package com.academy.shop.service;

import com.academy.shop.dto.cart.CartDto;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    CartDto getCart(HttpSession session);

    void addItem(HttpSession session, Integer itemId, Integer amount);

    void changeAmount(HttpSession session, Integer itemId, Integer amount);

    void removeItem(HttpSession session, Integer itemId);

    void clear(HttpSession session);
}
