package com.academy.shop.filter;

import com.academy.shop.service.session_manager.CartSessionManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class SessionActivityFilter extends OncePerRequestFilter {
    private final CartSessionManager cartSessionManager;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            cartSessionManager.updateInteraction(session);
        }
        filterChain.doFilter(req, resp);
    }
}
