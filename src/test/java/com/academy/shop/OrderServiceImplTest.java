package com.academy.shop;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.dto.cart.CartItemDto;
import com.academy.shop.dto.order.response.OrderPageDto;
import com.academy.shop.model.entity.*;
import com.academy.shop.model.repository.ItemRepository;
import com.academy.shop.model.repository.OrderItemRepository;
import com.academy.shop.model.repository.OrderRepository;
import com.academy.shop.model.repository.UserRepository;
import com.academy.shop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(
                orderRepository,
                userRepository,
                itemRepository,
                orderItemRepository
        );
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setBalance(5000F);
        return user;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setOrderId(1);
        order.setUser(createUser());
        order.setStatus(Status.STATUS_READY);
        order.setTotalPrice(1000F);
        order.setItems(new HashSet<>());
        return order;
    }

    @Test
    void createOrder_whenCartEmpty_shouldThrowException() {
        CartDto cart = new CartDto();
        assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(cart, 1));
        verifyNoInteractions(orderRepository);
    }

    @Test
    void createOrder_shouldCreateOrderAndOrderItems() {
        User user = createUser();
        CartDto cart = new CartDto();
        cart.getItems().add(
                new CartItemDto(
                        10,
                        "Телефон",
                        1000F,
                        2
                )
        );
        Item item = new Item();
        item.setItemId(10);
        item.setItemName("Телефон");
        when(userRepository.getReferenceById(1)).thenReturn(user);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order =
                            invocation.getArgument(0);
                    order.setOrderId(100);
                    return order;
                });
        when(itemRepository.getReferenceById(10)).thenReturn(item);
        OrderPageDto result = orderService.createOrder(cart, 1);
        assertEquals(100, result.getOrderId());
        assertEquals(Status.STATUS_CREATED, result.getStatus());
        assertEquals(2000F, result.getTotalPrice());

        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void findOrderByIdAndUser_whenOrderDoesNotBelongToUser()
            throws Exception {
        when(orderRepository.findByOrderIdAndUserId(1, 2)).thenReturn(null);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                        orderService.findOrderByIdAndUser(1, 2));
        assertEquals("Заказ не принадлежит текущему пользователю", exception.getMessage());
    }

    @Test
    void findOrderByIdAndUser_shouldReturnDto() {
        Order order = createOrder();
        when(orderRepository.findByOrderIdAndUserId(1, 1)).thenReturn(order);
        OrderPageDto result = orderService.findOrderByIdAndUser(1, 1);
        assertEquals(1, result.getOrderId());
        assertEquals(Status.STATUS_READY, result.getStatus());
        assertEquals(1000F, result.getTotalPrice());
    }

    @Test
    void pay_shouldPayReadyOrder() {
        Order order = createOrder();
        when(orderRepository.findByOrderIdAndUserId(1, 1)).thenReturn(order);
        orderService.pay(1, 1);
        assertEquals(Status.STATUS_DELIVERED_PAID, order.getStatus());
        assertEquals(4000F, order.getUser().getBalance());
        assertNotNull(order.getPayDate());
        verify(orderRepository).save(order);
    }

    @Test
    void pay_whenOrderAlreadyPaid_shouldThrowException() {
        Order order = createOrder();
        order.setStatus(Status.STATUS_DELIVERED_PAID);
        when(orderRepository.findByOrderIdAndUserId(1, 1))
                .thenReturn(order);
        assertThrows(IllegalStateException.class, () -> orderService.pay(1, 1));
        verify(orderRepository, never()).save(order);
    }

    @Test
    void pay_whenOrderCanceled_shouldThrowException() {
        Order order = createOrder();
        order.setStatus(Status.STATUS_CANCELED);
        when(orderRepository.findByOrderIdAndUserId(1, 1))
                .thenReturn(order);
        assertThrows(IllegalStateException.class, () ->
                orderService.pay(1, 1));
    }

    @Test
    void pay_whenOrderNotReady_shouldThrowException() {
        Order order = createOrder();
        order.setStatus(Status.STATUS_FORMING);
        when(orderRepository.findByOrderIdAndUserId(1, 1))
                .thenReturn(order);
        assertThrows(IllegalStateException.class, () ->
                orderService.pay(1, 1));
    }

    @Test
    void pay_whenInsufficientMoney_shouldThrowException() {
        Order order = createOrder();
        order.getUser().setBalance(500F);
        when(orderRepository.findByOrderIdAndUserId(1, 1))
                .thenReturn(order);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                        orderService.pay(1, 1));
        assertEquals("Недостаточно средств", exception.getMessage());
        assertEquals(500F, order.getUser().getBalance());
    }

    @Test
    void cancel_shouldChangeStatusToCanceled() {
        Order order = createOrder();
        when(orderRepository.findByOrderIdAndUserId(1, 1))
                .thenReturn(order);
        orderService.cancel(1, 1);
        assertEquals(Status.STATUS_CANCELED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void cancel_whenOrderAlreadyPaid_shouldThrowException() {
        Order order = createOrder();
        order.setStatus(Status.STATUS_DELIVERED_PAID);
        when(orderRepository.findByOrderIdAndUserId(1, 1))
                .thenReturn(order);
        assertThrows(IllegalStateException.class, () ->
                orderService.cancel(1, 1));
    }

    @Test
    void cancel_whenOrderAlreadyCanceled_shouldThrowException() {
        Order order = createOrder();
        order.setStatus(Status.STATUS_CANCELED);
        when(orderRepository.findByOrderIdAndUserId(1, 1))
                .thenReturn(order);
        assertThrows(IllegalStateException.class, () ->
                orderService.cancel(1, 1));
    }

    @Test
    void changeComment_shouldChangeComment() {
        Order order = createOrder();
        when(orderRepository.getReferenceById(1)).thenReturn(order);
        orderService.changeComment(1, "Позвонить перед доставкой");
        assertEquals("Позвонить перед доставкой", order.getComment());
        verify(orderRepository).save(order);
    }

    @Test
    void changeOrderStatus_shouldChangeStatus() {
        Order order = createOrder();
        when(orderRepository.getReferenceById(1)).thenReturn(order);
        orderService.changeOrderStatus(1, Status.STATUS_SENT);
        assertEquals(Status.STATUS_SENT, order.getStatus());
    }
}
