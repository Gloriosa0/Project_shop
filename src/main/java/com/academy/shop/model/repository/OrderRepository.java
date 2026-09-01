package com.academy.shop.model.repository;

import com.academy.shop.model.entity.Order;
import com.academy.shop.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserId(Integer userId);

    Order findByOrderIdAndUserId(Integer orderId, Integer userId);

    @Query("""
      select o
      from Order o
      where (:userId is null or o.user.id = :userId)
      and (:status is null or o.status = :status)
      and (:from is null or o.payDate >= :from)
      and (:to is null or o.payDate <= :to)
      """)
    List<Order> search(
            @Param("userId") Integer userId,
            @Param("status") Status status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
