package com.academy.shop.service.session_manager;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CartSessionManager {

    private static final long CART_TIMEOUT_SECONDS = 10 * 60;

    private static final String LAST_INTERACTION = "cartLastInteraction";

    private static final String CART_ATTRIBUTE = "cart";

    private final Map<String, HttpSession> sessions =
            new ConcurrentHashMap<>();

    public void updateInteraction(HttpSession session) {
        try {
            session.setAttribute(LAST_INTERACTION, Instant.now());
            sessions.put(session.getId(), session);
        } catch (IllegalStateException e) {
            sessions.remove(session.getId());
        }
    }

    public void cleanExpiredCarts() {
        Instant now = Instant.now();
        sessions.forEach((sessionId, session) -> {
            try {
                Object value = session.getAttribute(LAST_INTERACTION);

                if (!(value instanceof Instant lastInteraction)) {
                    return;
                }
                long seconds = now.getEpochSecond() - lastInteraction.getEpochSecond();

                if (seconds >= CART_TIMEOUT_SECONDS) {
                    session.removeAttribute("cart");
                    session.removeAttribute(LAST_INTERACTION);
                    sessions.remove(sessionId);
                }
            } catch (IllegalStateException e) {
                sessions.remove(session.getId());
            }
        });

    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
