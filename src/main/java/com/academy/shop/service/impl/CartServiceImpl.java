package com.academy.shop.service.impl;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.dto.cart.CartItemDto;
import com.academy.shop.dto.item.response.ItemPageDto;
import com.academy.shop.service.CartService;
import com.academy.shop.service.ItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private static final String CART_ATTRIBUTE = "cart";

    private final ItemService itemService;

    public CartDto getCart(HttpSession session) {
        CartDto cartDto = (CartDto) session.getAttribute(CART_ATTRIBUTE);
        if (cartDto == null) {
            cartDto = new CartDto();
            session.setAttribute(CART_ATTRIBUTE, cartDto);
        }
        return cartDto;
    }

    @Transactional(readOnly = true)
    public void addItem(HttpSession session, Integer itemId, Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException(
                    "Количество должно быть больше нуля"
            );
        }

        ItemPageDto itemPageDto = itemService.findItemById(itemId);

        if (!Boolean.TRUE.equals(itemPageDto.getInStock())) {
            throw new IllegalArgumentException(
                    "Товар отсутствует на складе"
            );
        }

        CartDto cartDto = getCart(session);

        Optional<CartItemDto> existingItem =
                cartDto.getItems()
                        .stream()
                        .filter(cartItem ->
                                cartItem.getItemId()
                                        .equals(itemId)
                        )
                        .findFirst();

        if (existingItem.isPresent()) {
            CartItemDto cartItem =
                    existingItem.get();
            cartItem.setAmount(
                    cartItem.getAmount() + amount
            );
        } else {
            CartItemDto cartItem =
                    new CartItemDto(
                            itemPageDto.getItemId(),
                            itemPageDto.getItemName(),
                            itemPageDto.getPrice(),
                            amount
                    );
            cartDto.getItems().add(cartItem);
        }

        session.setAttribute(CART_ATTRIBUTE, cartDto);
    }

    public void changeAmount(HttpSession session, Integer itemId, Integer amount) {
        if (amount == null || amount <= 0) {
            removeItem(session, itemId);
            return;
        }

        CartDto cart = getCart(session);

        cart.getItems()
                .stream()
                .filter(item ->
                        item.getItemId()
                                .equals(itemId)
                )
                .findFirst()
                .ifPresent(item ->
                        item.setAmount(amount)
                );

        session.setAttribute(CART_ATTRIBUTE, cart);
    }

    public void removeItem(HttpSession session, Integer itemId) {
        CartDto cart = getCart(session);
        cart.getItems()
                .removeIf(item ->
                        item.getItemId()
                                .equals(itemId)
                );
        session.setAttribute(CART_ATTRIBUTE, cart);
    }

    public void clear(HttpSession session) {
        session.removeAttribute(CART_ATTRIBUTE);
    }
}
