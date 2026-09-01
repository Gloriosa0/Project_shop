package com.academy.shop;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.dto.cart.CartItemDto;
import com.academy.shop.dto.item.response.ItemPageDto;
import com.academy.shop.service.ItemService;
import com.academy.shop.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private ItemService itemService;
    private CartServiceImpl cartService;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(itemService);
        session = new MockHttpSession();
    }

    @Test
    void getCart_whenCartDoesNotExist_shouldCreateCart() {
        CartDto cart = cartService.getCart(session);
        assertNotNull(cart);
        assertTrue(cart.getItems().isEmpty());
        assertSame(cart, session.getAttribute("cart"));
    }

    @Test
    void getCart_whenCartExists_shouldReturnExistingCart() {
        CartDto expected = new CartDto();
        session.setAttribute("cart", expected);
        CartDto actual = cartService.getCart(session);
        assertSame(expected, actual);
    }

    @Test
    void addItem_shouldAddNewItem() {
        ItemPageDto item = ItemPageDto.builder()
                .itemId(1)
                .itemName("Телефон")
                .price(1000F)
                .inStock(true)
                .build();

        when(itemService.findItemById(1)).thenReturn(item);
        cartService.addItem(session, 1, 2);
        CartDto cart = cartService.getCart(session);
        assertEquals(1, cart.getItems().size());
        CartItemDto cartItem = cart.getItems().get(0);
        assertEquals(1, cartItem.getItemId());
        assertEquals("Телефон", cartItem.getItemName());
        assertEquals(1000F, cartItem.getPricePerItem());
        assertEquals(2, cartItem.getAmount());
    }

    @Test
    void addItem_whenItemAlreadyExists_shouldIncreaseAmount() {
        ItemPageDto item = ItemPageDto.builder()
                .itemId(1)
                .itemName("Телефон")
                .price(1000F)
                .inStock(true)
                .build();

        when(itemService.findItemById(1)).thenReturn(item);
        cartService.addItem(session, 1, 2);
        cartService.addItem(session, 1, 3);
        CartDto cart = cartService.getCart(session);
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getAmount());
    }

    @Test
    void addItem_whenAmountZero_shouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> cartService.addItem(session, 1, 0)
        );

        verifyNoInteractions(itemService);
    }

    @Test
    void addItem_whenAmountNegative_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(session, 1, -1));
        verifyNoInteractions(itemService);
    }

    @Test
    void addItem_whenItemNotInStock_shouldThrowException() {
        ItemPageDto item = ItemPageDto.builder()
                .itemId(1)
                .itemName("Телефон")
                .price(1000F)
                .inStock(false)
                .build();

        when(itemService.findItemById(1)).thenReturn(item);
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cartService.addItem(session, 1, 1)
                );

        assertEquals("Товар отсутствует на складе", exception.getMessage());
    }

    @Test
    void changeAmount_shouldChangeExistingItemAmount() {
        ItemPageDto item = ItemPageDto.builder()
                .itemId(1)
                .itemName("Телефон")
                .price(1000F)
                .inStock(true)
                .build();

        when(itemService.findItemById(1)).thenReturn(item);
        cartService.addItem(session, 1, 2);
        cartService.changeAmount(session, 1, 7);
        CartDto cart = cartService.getCart(session);
        assertEquals(7, cart.getItems().get(0).getAmount());
    }

    @Test
    void changeAmount_whenAmountZero_shouldRemoveItem() {
        ItemPageDto item = ItemPageDto.builder()
                .itemId(1)
                .itemName("Телефон")
                .price(1000F)
                .inStock(true)
                .build();

        when(itemService.findItemById(1)).thenReturn(item);
        cartService.addItem(session, 1, 2);
        cartService.changeAmount(session, 1, 0);
        assertTrue(
                cartService.getCart(session)
                        .getItems()
                        .isEmpty()
        );
    }

    @Test
    void removeItem_shouldRemoveItemFromCart() {
        ItemPageDto item = ItemPageDto.builder()
                .itemId(1)
                .itemName("Телефон")
                .price(1000F)
                .inStock(true)
                .build();

        when(itemService.findItemById(1)).thenReturn(item);
        cartService.addItem(session, 1, 2);
        cartService.removeItem(session, 1);
        assertTrue(
                cartService.getCart(session)
                        .getItems()
                        .isEmpty()
        );
    }

    @Test
    void clear_shouldRemoveCartFromSession() {
        cartService.getCart(session);
        assertNotNull(session.getAttribute("cart"));
        cartService.clear(session);
        assertNull(session.getAttribute("cart"));
    }
}
