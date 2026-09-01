package com.academy.shop.service;

import com.academy.shop.dto.item.request.ItemCreateUpdateDto;
import com.academy.shop.dto.item.response.ItemPageDto;
import com.academy.shop.dto.item.response.ItemShortDto;

import java.time.LocalDate;
import java.util.List;

public interface ItemService {
    ItemPageDto createItem(ItemCreateUpdateDto itemCreateUpdateDto);

    List<ItemShortDto> findAllItems();

    ItemPageDto findItemById(Integer itemId);

    List<ItemShortDto> findItemsByStock();

    List<ItemShortDto> findItemsByNameByStock(String itemName, Boolean inStock);

    List<ItemShortDto> findItemsByNameAndStockTrue(String itemName);

    List<ItemShortDto> findItemsFiltered(
            String itemName,
            Boolean inStock,
            Float minPrice,
            Float maxPrice,
            String seller,
            LocalDate minDate,
            LocalDate maxDate);

    ItemPageDto updateItem(Integer itemId, ItemCreateUpdateDto itemCreateUpdateDto);

    void deleteFromStockItem(Integer itemId);
}
