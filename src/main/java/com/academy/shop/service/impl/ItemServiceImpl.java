package com.academy.shop.service.impl;

import com.academy.shop.dto.item.request.ItemCreateUpdateDto;
import com.academy.shop.dto.item.response.ItemPageDto;
import com.academy.shop.dto.item.response.ItemShortDto;
import com.academy.shop.model.entity.Item;
import com.academy.shop.model.repository.ItemRepository;
import com.academy.shop.model.repository.specification.ItemSpecification;
import com.academy.shop.model.repository.specification.SearchCriteria;
import com.academy.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemPageDto createItem(ItemCreateUpdateDto itemCreateUpdateDto) {
        Item item = new Item();
        item.setItemName(itemCreateUpdateDto.getItemName());
        item.setPrice(itemCreateUpdateDto.getPrice());
        item.setInStock(itemCreateUpdateDto.getInStock());
        item.setSeller(itemCreateUpdateDto.getSeller());
        if (itemCreateUpdateDto.getDescription() != null) {
            item.setDescription(itemCreateUpdateDto.getDescription());
        }
        if (itemCreateUpdateDto.getArrivalDate() != null) {
            item.setArrivalDate(itemCreateUpdateDto.getArrivalDate());
        }

        Item result = itemRepository.save(item);

        return ItemPageDto.builder()
                .itemId(result.getItemId())
                .itemName(result.getItemName())
                .price(result.getPrice())
                .inStock(result.getInStock())
                .seller(result.getSeller())
                .description(result.getDescription())
                .arrivalDate(result.getArrivalDate())
                .build();
    }

    @Override
    public List<ItemShortDto> findAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemShortDto> itemShortDtos = new ArrayList<>();
        for (Item item : items) {
            ItemShortDto itemShortDto = new ItemShortDto();
            itemShortDto.setItemId(item.getItemId());
            itemShortDto.setItemName(item.getItemName());
            itemShortDto.setPrice(item.getPrice());
            itemShortDto.setInStock(item.getInStock());
            itemShortDto.setArrivalDate(item.getArrivalDate());
            itemShortDtos.add(itemShortDto);
        }
        return itemShortDtos;
    }

    @Override
    public ItemPageDto findItemById(Integer itemId) {
        Item item = itemRepository.getReferenceById(itemId);
        return ItemPageDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .inStock(item.getInStock())
                .seller(item.getSeller())
                .description(item.getDescription())
                .arrivalDate(item.getArrivalDate())
                .build();
    }

    @Override
    public List<ItemShortDto> findItemsByStock() {
        List<Item> items = itemRepository.findByInStockTrue();
        List<ItemShortDto> itemShortDtos = new ArrayList<>();
        for (Item item : items) {
            ItemShortDto itemShortDto = new ItemShortDto();
            itemShortDto.setItemId(item.getItemId());
            itemShortDto.setItemName(item.getItemName());
            itemShortDto.setPrice(item.getPrice());
            itemShortDto.setInStock(item.getInStock());
            itemShortDto.setArrivalDate(item.getArrivalDate());
            itemShortDtos.add(itemShortDto);
        }
        return itemShortDtos;
    }

    @Override
    public List<ItemShortDto> findItemsByNameByStock(String itemName, Boolean inStock) {
        List<Item> items = new ArrayList<>();
        if (itemName != null && !itemName.isBlank()
                && inStock != null) {
            items.addAll(itemRepository.findByItemNameContainingIgnoreCaseAndInStock(
                    itemName,
                    inStock
            ));
        } else if (itemName != null && !itemName.isBlank()) {
            items.addAll(itemRepository.findByItemNameContainingIgnoreCase(itemName));
        } else if (inStock != null) {
            items.addAll(itemRepository.findByInStock(inStock));
        } else {
            items.addAll(itemRepository.findAll());
        }
        List<ItemShortDto> itemShortDtos = new ArrayList<>();
        for (Item item : items) {
            ItemShortDto itemShortDto = new ItemShortDto();
            itemShortDto.setItemId(item.getItemId());
            itemShortDto.setItemName(item.getItemName());
            itemShortDto.setPrice(item.getPrice());
            itemShortDto.setInStock(item.getInStock());
            itemShortDto.setArrivalDate(item.getArrivalDate());
            itemShortDtos.add(itemShortDto);
        }
        return itemShortDtos;
    }

    @Override
    public List<ItemShortDto> findItemsByNameAndStockTrue(String itemName) {
        List<Item> items = itemRepository.findByItemNameContainingIgnoreCaseAndInStockTrue(itemName);
        List<ItemShortDto> itemShortDtos = new ArrayList<>();
        for (Item item : items) {
            ItemShortDto itemShortDto = new ItemShortDto();
            itemShortDto.setItemId(item.getItemId());
            itemShortDto.setItemName(item.getItemName());
            itemShortDto.setPrice(item.getPrice());
            itemShortDto.setInStock(item.getInStock());
            itemShortDto.setArrivalDate(item.getArrivalDate());
            itemShortDtos.add(itemShortDto);
        }
        return itemShortDtos;
    }

    @Override
    public List<ItemShortDto> findItemsFiltered(
            String itemName,
            Boolean inStock,
            Float minPrice,
            Float maxPrice,
            String seller,
            LocalDate minDate,
            LocalDate maxDate) {
        SearchCriteria sc = new SearchCriteria(itemName, inStock, minPrice, maxPrice, seller, minDate, maxDate);
        ItemSpecification itemSpecification = new ItemSpecification(sc);
        List<Item> items = itemRepository.findAll(itemSpecification);
        List<ItemShortDto> itemShortDtos = new ArrayList<>();
        for (Item item : items) {
            ItemShortDto itemShortDto = new ItemShortDto();
            itemShortDto.setItemId(item.getItemId());
            itemShortDto.setItemName(item.getItemName());
            itemShortDto.setPrice(item.getPrice());
            itemShortDto.setInStock(item.getInStock());
            itemShortDto.setArrivalDate(item.getArrivalDate());
            itemShortDtos.add(itemShortDto);
        }
        return itemShortDtos;
    }

    @Transactional
    @Override
    public ItemPageDto updateItem(Integer itemId, ItemCreateUpdateDto itemCreateUpdateDto) {
        Item item = itemRepository.getReferenceById(itemId);
        if (itemCreateUpdateDto.getItemName() != null) {
            item.setItemName(itemCreateUpdateDto.getItemName());
        }
        if (itemCreateUpdateDto.getPrice() != null) {
            item.setPrice(itemCreateUpdateDto.getPrice());
        }
        if (itemCreateUpdateDto.getInStock() != null) {
            item.setInStock(itemCreateUpdateDto.getInStock());
        }
        if (itemCreateUpdateDto.getSeller() != null) {
            item.setSeller(itemCreateUpdateDto.getSeller());
        }
        if (itemCreateUpdateDto.getDescription() != null) {
            item.setDescription(itemCreateUpdateDto.getDescription());
        }
        if (itemCreateUpdateDto.getArrivalDate() != null) {
            item.setArrivalDate(itemCreateUpdateDto.getArrivalDate());
        }
        Item result = itemRepository.save(item);

        return ItemPageDto.builder()
                .itemId(result.getItemId())
                .itemName(result.getItemName())
                .price(result.getPrice())
                .inStock(result.getInStock())
                .seller(result.getSeller())
                .description(result.getDescription())
                .arrivalDate(result.getArrivalDate())
                .build();
    }

    @Transactional
    @Override
    public void deleteFromStockItem(Integer itemId) {
        Item item = itemRepository.getReferenceById(itemId);
        item.setInStock(false);
        itemRepository.save(item);
    }
}
