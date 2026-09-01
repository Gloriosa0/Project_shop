package com.academy.shop.dto.order.response;

import com.academy.shop.model.entity.OrderItem;
import com.academy.shop.model.entity.Status;
import com.academy.shop.model.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class OrderPageDto {
    private Integer orderId;

    private LocalDateTime payDate;

    private User user;

    private Status status;

    private Float totalPrice;

    private String comment;

    private Set<OrderItem> items;
}
