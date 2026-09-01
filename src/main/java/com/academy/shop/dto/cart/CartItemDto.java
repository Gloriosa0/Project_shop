package com.academy.shop.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Integer itemId;
    private String itemName;
    private Float pricePerItem;
    private Integer amount;

    public Float getTotalPrice() {
        return pricePerItem * amount;
    }
}
