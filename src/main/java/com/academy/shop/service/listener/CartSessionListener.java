package com.academy.shop.service.listener;

import com.academy.shop.service.session_manager.CartSessionManager;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartSessionListener implements HttpSessionListener {

    private final CartSessionManager cartSessionManager;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        cartSessionManager.removeSession(
                event.getSession().getId()
        );
    }
}
