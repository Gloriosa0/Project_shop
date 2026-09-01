package com.academy.shop.dto.item.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemShortDto {
    private Integer itemId;
    private String itemName;
    private Float price;
    private Boolean inStock;
    private LocalDate arrivalDate;
}
