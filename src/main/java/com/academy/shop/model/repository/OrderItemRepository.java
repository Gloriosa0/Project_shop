package com.academy.shop.model.repository;

import com.academy.shop.model.entity.OrderItem;
import com.academy.shop.model.entity.key.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey> {

}
