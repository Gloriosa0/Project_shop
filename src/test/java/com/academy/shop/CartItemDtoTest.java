package com.academy.shop;

import com.academy.shop.dto.cart.CartItemDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemDtoTest {

    @Test
    void getTotalPrice_shouldMultiplyPriceByAmount() {
        CartItemDto item = new CartItemDto(1, "Ноутбук", 1500F, 2);
        assertEquals(3000F, item.getTotalPrice());
    }
}
