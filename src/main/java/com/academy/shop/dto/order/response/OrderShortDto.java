package com.academy.shop.dto.order.response;

import com.academy.shop.model.entity.Status;
import com.academy.shop.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderShortDto {
    private Integer orderId;

    private LocalDateTime payDate;

    private User user;

    private Status status;

    private Float totalPrice;

    private String comment;
}
