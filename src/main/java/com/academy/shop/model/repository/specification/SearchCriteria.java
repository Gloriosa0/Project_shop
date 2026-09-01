package com.academy.shop.model.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private String itemName;
    private Boolean inStock;
    private Float minPrice;
    private Float maxPrice;
    private String seller;
    private LocalDate minArrivalDate;
    private LocalDate maxArrivalDate;
}
