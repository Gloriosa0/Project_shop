package com.academy.shop.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_name")
    private String itemName;

    private Float price;

    @Column(name = "in_stock")
    private Boolean inStock;

    private String seller;

    private String description;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;
}
