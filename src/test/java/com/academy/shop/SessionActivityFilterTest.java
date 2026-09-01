package com.academy.shop;

import com.academy.shop.dto.cart.CartDto;
import com.academy.shop.service.session_manager.CartSessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CartSessionManagerTest {

    private CartSessionManager manager;

    @BeforeEach
    void setUp() {
        manager = new CartSessionManager();
    }

    @Test
    void updateInteraction_shouldStoreSession() {
        MockHttpSession session = new MockHttpSession();
        manager.updateInteraction(session);
        assertNotNull(session.getAttribute("cartLastInteraction"));
    }

    @Test
    void cleanExpiredCarts_shouldRemoveExpiredCart() {
        MockHttpSession session = new MockHttpSession();
        CartDto cart = new CartDto();
        session.setAttribute("cart", cart);
        session.setAttribute("cartLastInteraction", Instant.now().minusSeconds(601));
        manager.updateInteraction(session);
        session.setAttribute("cartLastInteraction", Instant.now().minusSeconds(601));
        manager.cleanExpiredCarts();
        assertNull(session.getAttribute("cart"));
        assertNull(session.getAttribute("cartLastInteraction"));
    }

    @Test
    void cleanExpiredCarts_shouldNotRemoveFreshCart() {
        MockHttpSession session = new MockHttpSession();
        CartDto cart = new CartDto();
        session.setAttribute("cart", cart);
        session.setAttribute("cartLastInteraction", Instant.now());
        manager.updateInteraction(session);
        manager.cleanExpiredCarts();
        assertNotNull(session.getAttribute("cart"));
    }

    @Test
    void removeSession_shouldNotThrowException() {
        MockHttpSession session = new MockHttpSession();
        manager.updateInteraction(session);
        assertDoesNotThrow(() -> manager.removeSession(session.getId()));
    }
}
