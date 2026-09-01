package com.academy.shop.dto.cart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDto {
    private List<CartItemDto> items = new ArrayList<>();

    public Float getTotalPrice() {
        return (float) items.stream()
                .mapToDouble(item ->
                        item.getPricePerItem()
                                * item.getAmount()
                )
                .sum();
    }

    public int getTotalAmount() {
        return items.stream()
                .mapToInt(CartItemDto::getAmount)
                .sum();
    }
}
