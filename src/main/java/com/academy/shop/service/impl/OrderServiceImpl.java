package com.academy.shop.service.impl;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.dto.cart.CartItemDto;
import com.academy.shop.dto.order.response.OrderPageDto;
import com.academy.shop.dto.order.response.OrderShortDto;
import com.academy.shop.model.entity.*;
import com.academy.shop.model.entity.key.OrderItemKey;
import com.academy.shop.model.repository.ItemRepository;
import com.academy.shop.model.repository.OrderItemRepository;
import com.academy.shop.model.repository.OrderRepository;
import com.academy.shop.model.repository.UserRepository;
import com.academy.shop.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    @Override
    public OrderPageDto createOrder(CartDto cartDto, Integer userId) {
        if (cartDto.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Нельзя оформить пустую корзину"
            );
        }
        Order order = new Order();
        order.setUser(userRepository.getReferenceById(userId));
        order.setPayDate(null);
        order.setTotalPrice(cartDto.getTotalPrice());
        order.setComment(null);
        order.setStatus(Status.STATUS_CREATED);
        order.setItems(new HashSet<>());

        Order result = orderRepository.save(order);

        for (CartItemDto cartItem : cartDto.getItems()) {
            Item item = itemRepository.getReferenceById(cartItem.getItemId());

            OrderItemKey key = new OrderItemKey();
            key.setOrderId(result.getOrderId());
            key.setItemId(item.getItemId());

            OrderItem orderItem = new OrderItem();
            orderItem.setId(key);
            orderItem.setOrder(result);
            orderItem.setItem(item);
            orderItem.setAmount(cartItem.getAmount());
            orderItem.setPricePerItem(cartItem.getPricePerItem());

            orderItemRepository.save(orderItem);
            result.getItems().add(orderItem);
        }

        return OrderPageDto.builder()
                .orderId(result.getOrderId())
                .payDate(result.getPayDate())
                .user(result.getUser())
                .status(result.getStatus())
                .totalPrice(result.getTotalPrice())
                .comment(result.getComment())
                .items(result.getItems())
                .build();
    }

    @Override
    public OrderPageDto findOrderByIdAndUser(Integer id, Integer userId) {
        Order order = orderRepository.findByOrderIdAndUserId(id, userId);
        if (order == null) {
            throw new IllegalArgumentException(
                    "Заказ не принадлежит текущему пользователю"
            );
        }
        return OrderPageDto.builder()
                .orderId(order.getOrderId())
                .payDate(order.getPayDate())
                .user(order.getUser())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .comment(order.getComment())
                .items(order.getItems())
                .build();
    }

    @Override
    public OrderPageDto findOrderById(Integer id) {
        Order order = orderRepository.getReferenceById(id);
        return OrderPageDto.builder()
                .orderId(order.getOrderId())
                .payDate(order.getPayDate())
                .user(order.getUser())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .comment(order.getComment())
                .items(order.getItems())
                .build();
    }

    @Override
    public List<OrderShortDto> findOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderShortDto> orderShortDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderShortDto orderShortDto = new OrderShortDto();
            orderShortDto.setOrderId(order.getOrderId());
            orderShortDto.setPayDate(order.getPayDate());
            orderShortDto.setUser(order.getUser());
            orderShortDto.setStatus(order.getStatus());
            orderShortDto.setTotalPrice(order.getTotalPrice());
            orderShortDto.setComment(order.getComment());
            orderShortDtos.add(orderShortDto);
        }
        return orderShortDtos;
    }

    @Override
    public List<OrderShortDto> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderShortDto> orderShortDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderShortDto orderShortDto = new OrderShortDto();
            orderShortDto.setOrderId(order.getOrderId());
            orderShortDto.setPayDate(order.getPayDate());
            orderShortDto.setUser(order.getUser());
            orderShortDto.setStatus(order.getStatus());
            orderShortDto.setTotalPrice(order.getTotalPrice());
            orderShortDto.setComment(order.getComment());
            orderShortDtos.add(orderShortDto);
        }
        return orderShortDtos;
    }

    @Override
    public List<OrderShortDto> findOrdersByParams(
            Integer id,
            Status status,
            LocalDateTime payDateFrom,
            LocalDateTime payDateTo) {
        List<Order> orders = orderRepository.search(id, status, payDateFrom, payDateTo);
        List<OrderShortDto> orderShortDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderShortDto orderShortDto = new OrderShortDto();
            orderShortDto.setOrderId(order.getOrderId());
            orderShortDto.setPayDate(order.getPayDate());
            orderShortDto.setUser(order.getUser());
            orderShortDto.setStatus(order.getStatus());
            orderShortDto.setTotalPrice(order.getTotalPrice());
            orderShortDto.setComment(order.getComment());
            orderShortDtos.add(orderShortDto);
        }
        return orderShortDtos;
    }

    @Transactional
    @Override
    public void changeOrderStatus(Integer id, Status status) {
        Order order = orderRepository.getReferenceById(id);

        if (!List.of(Status.values()).contains(status)) {
            throw new IllegalArgumentException(
                    "Статус не указан"
            );
        }
        order.setStatus(status);
    }

    @Transactional
    @Override
    public void changeComment(Integer id, String comment) {
        Order order = orderRepository.getReferenceById(id);
        order.setComment(comment);

        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void pay(Integer id, Integer userId) {
        Order order = orderRepository.findByOrderIdAndUserId(id, userId);
        if (order == null) {
            throw new IllegalArgumentException(
                    "Заказ не принадлежит текущему пользователю"
            );
        }
        Status currentStatus = order.getStatus();
        if (currentStatus == Status.STATUS_DELIVERED_PAID) {
            throw new IllegalStateException(
                    "Заказ уже оплачен");
        }
        if (currentStatus == Status.STATUS_REJECTED_UNPAID
                || currentStatus == Status.STATUS_RETURNED
                || currentStatus == Status.STATUS_CANCELED) {
            throw new IllegalStateException(
                    "Заказ не подлежит оплате");
        }
        if (currentStatus != Status.STATUS_READY) {
            throw new IllegalStateException(
                    "Заказ в процессе доставки");
        }
        if ((order.getUser().getBalance() - order.getTotalPrice()) < 0) {
            throw new IllegalStateException(
                    "Недостаточно средств");
        }
        order.getUser().setBalance(order.getUser().getBalance() - order.getTotalPrice());
        order.setStatus(Status.STATUS_DELIVERED_PAID);
        order.setPayDate(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void cancel(Integer id, Integer userId) {
        Order order = orderRepository.findByOrderIdAndUserId(id, userId);
        if (order == null) {
            throw new IllegalArgumentException(
                    "Заказ не принадлежит текущему пользователю"
            );
        }
        if (order.getStatus() == Status.STATUS_DELIVERED_PAID ||
                order.getStatus() == Status.STATUS_REJECTED_UNPAID ||
                order.getStatus() == Status.STATUS_CANCELED ||
                order.getStatus() == Status.STATUS_RETURNED) {
            throw new IllegalStateException(
                    "Невозможно отменить заказ");
        }
        order.setStatus(Status.STATUS_CANCELED);
        orderRepository.save(order);
    }
}
