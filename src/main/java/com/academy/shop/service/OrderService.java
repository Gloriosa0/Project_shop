package com.academy.shop.service;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.dto.order.response.OrderPageDto;
import com.academy.shop.dto.order.response.OrderShortDto;
import com.academy.shop.model.entity.Order;
import com.academy.shop.model.entity.Status;
import com.academy.shop.model.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderPageDto createOrder(CartDto cart, Integer userId);

    OrderPageDto findOrderByIdAndUser(Integer id, Integer userId);

    OrderPageDto findOrderById(Integer id);

    List<OrderShortDto> findOrdersByUserId(Integer userId);

    List<OrderShortDto> findAllOrders();

    List<OrderShortDto> findOrdersByParams(Integer id, Status status, LocalDateTime payDateFrom, LocalDateTime payDateTo);

    void changeOrderStatus(Integer orderId, Status status);

    void changeComment(Integer orderId, String comment);

    void pay(Integer orderId, Integer userId);

    void cancel(Integer orderId, Integer userId);
}
