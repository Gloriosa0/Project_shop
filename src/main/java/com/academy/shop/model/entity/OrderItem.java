package com.academy.shop.model.entity;

import com.academy.shop.model.entity.key.OrderItemKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemKey id;

    @ToString.Exclude
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ToString.Exclude
    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer amount;

    @Column(name = "price_per_item")
    private Float pricePerItem;
}
