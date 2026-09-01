package com.academy.shop.config;

import com.academy.shop.filter.SessionActivityFilter;
import com.academy.shop.service.session_manager.CartSessionManager;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SessionActivityFilter sessionActivityFilter
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .dispatcherTypeMatchers(
                                        DispatcherType.FORWARD,
                                        DispatcherType.ERROR
                                ).permitAll()
                                .requestMatchers(
                                        "/",
                                        "/login",
                                        "/registration",
                                        "/error/**"
                                ).permitAll()
                                .requestMatchers("/items/admin/**").hasRole("ADMIN")
                                .requestMatchers("/orders/admin/**").hasRole("ADMIN")
                                .requestMatchers("/admin/users/**").hasRole("ADMIN")

                                .requestMatchers("/items/**").authenticated()
                                .requestMatchers("/cart/**").authenticated()
                                .requestMatchers("/orders/**").authenticated()
                                .requestMatchers("/profile/**").authenticated()

                                .anyRequest().authenticated()
                ).formLogin(form ->
                        form
                                .defaultSuccessUrl("/items", true)
                                .permitAll()
                ).exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler((request, response, exception) -> {
                                    response.sendRedirect(
                                            request.getContextPath() + "/error/403"
                                    );
                                })
                ).addFilterAfter(
                        sessionActivityFilter,
                        SecurityContextHolderFilter.class
                );
        return http.build();
    }

    @Bean
    public SessionActivityFilter sessionActivityFilter(CartSessionManager cartSessionManager) {
        return new SessionActivityFilter(cartSessionManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
