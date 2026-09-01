package com.academy.shop.service.sheduler;

import com.academy.shop.service.session_manager.CartSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartCleanupScheduler {

    private final CartSessionManager cartSessionManager;

    @Scheduled(fixedRate = 60_000)
    public void cleanCarts() {
        cartSessionManager.cleanExpiredCarts();
    }
}
