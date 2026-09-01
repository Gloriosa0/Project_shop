package com.academy.shop;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.dto.cart.CartItemDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartDtoTest {

    @Test
    void getTotalPrice_shouldCalculateCorrectly() {
        CartDto cart = new CartDto();
        cart.getItems().add(new CartItemDto(1, "Телефон", 1000F, 2));
        cart.getItems().add(new CartItemDto(2, "Чехол", 500F, 3));
        assertEquals(3500F, cart.getTotalPrice());
    }

    @Test
    void getTotalAmount_shouldCalculateCorrectly() {
        CartDto cart = new CartDto();
        cart.getItems().add(new CartItemDto(1, "Телефон", 1000F, 2));
        cart.getItems().add(new CartItemDto(2, "Чехол", 500F, 3));
        assertEquals(5, cart.getTotalAmount());
    }

    @Test
    void emptyCart_shouldHaveZeroTotal() {
        CartDto cart = new CartDto();
        assertEquals(0F, cart.getTotalPrice());
        assertEquals(0, cart.getTotalAmount());
    }
}
