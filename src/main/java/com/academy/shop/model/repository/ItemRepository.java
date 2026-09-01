package com.academy.shop.model.repository;

import com.academy.shop.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer>,
        JpaSpecificationExecutor<Item> {

    List<Item> findByItemNameContainingIgnoreCase(String itemName);

    List<Item> findByInStockTrue();

    List<Item> findByInStock(Boolean inStock);

    List<Item> findByItemNameContainingIgnoreCaseAndInStockTrue(String itemName);

    List<Item> findByItemNameContainingIgnoreCaseAndInStock(String itemName, Boolean inStock);

}
