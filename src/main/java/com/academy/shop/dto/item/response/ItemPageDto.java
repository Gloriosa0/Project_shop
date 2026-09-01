package com.academy.shop.dto.item.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ItemPageDto {
    private Integer itemId;
    private String itemName;
    private Float price;
    private Boolean inStock;
    private String seller;
    private String description;
    private LocalDate arrivalDate;
}
