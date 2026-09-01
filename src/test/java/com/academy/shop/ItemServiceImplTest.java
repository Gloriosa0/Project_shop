package com.academy.shop;

import com.academy.shop.dto.item.request.ItemCreateUpdateDto;
import com.academy.shop.dto.item.response.ItemPageDto;
import com.academy.shop.dto.item.response.ItemShortDto;
import com.academy.shop.model.entity.Item;
import com.academy.shop.model.repository.ItemRepository;
import com.academy.shop.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository);
    }

    private Item createItem() {
        Item item = new Item();
        item.setItemId(1);
        item.setItemName("Телефон");
        item.setPrice(1000F);
        item.setInStock(true);
        item.setSeller("Apple");
        item.setDescription("Смартфон");
        item.setArrivalDate(LocalDate.of(2026, 9, 1));

        return item;
    }

    @Test
    void createItem_shouldSaveAndReturnDto() {
        ItemCreateUpdateDto dto =
                ItemCreateUpdateDto.builder()
                        .itemName("Телефон")
                        .price(1000F)
                        .inStock(true)
                        .seller("Apple")
                        .description("Смартфон")
                        .arrivalDate(LocalDate.of(2026, 9, 1))
                        .build();

        Item saved = createItem();
        when(itemRepository.save(any(Item.class)))
                .thenReturn(saved);
        ItemPageDto result =
                itemService.createItem(dto);
        assertEquals(1, result.getItemId());
        assertEquals("Телефон", result.getItemName());
        assertEquals(1000F, result.getPrice());
        assertTrue(result.getInStock());
        assertEquals("Apple", result.getSeller());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void findAllItems_shouldConvertEntitiesToDtos() {
        when(itemRepository.findAll()).thenReturn(List.of(createItem()));
        List<ItemShortDto> result = itemService.findAllItems();
        assertEquals(1, result.size());
        assertEquals("Телефон", result.get(0).getItemName());
        assertEquals(1000F, result.get(0).getPrice());
    }

    @Test
    void findItemById_shouldReturnDto() {
        Item item = createItem();
        when(itemRepository.getReferenceById(1)).thenReturn(item);
        ItemPageDto result = itemService.findItemById(1);
        assertEquals(1, result.getItemId());
        assertEquals("Телефон", result.getItemName());
        assertEquals(1000F, result.getPrice());
        assertEquals("Apple", result.getSeller());
        verify(itemRepository)
                .getReferenceById(1);
    }

    @Test
    void findItemsByStock_shouldUseRepositoryMethod() {
        when(itemRepository.findByInStockTrue())
                .thenReturn(List.of(createItem()));
        List<ItemShortDto> result = itemService.findItemsByStock();
        assertEquals(1, result.size());
        verify(itemRepository).findByInStockTrue();
    }

    @Test
    void findItemsByNameByStock_whenBothParametersProvided() {
        when(itemRepository.findByItemNameContainingIgnoreCaseAndInStock("phone", true)).
                thenReturn(List.of(createItem()));
        List<ItemShortDto> result = itemService.findItemsByNameByStock("phone", true);
        assertEquals(1, result.size());
        verify(itemRepository).findByItemNameContainingIgnoreCaseAndInStock("phone", true);
}

    @Test
    void findItemsByNameByStock_whenNoParameters_shouldFindAll() {
        when(itemRepository.findAll()).thenReturn(List.of(createItem()));
        List<ItemShortDto> result = itemService.findItemsByNameByStock(null, null);
        assertEquals(1, result.size());
        verify(itemRepository).findAll();
    }

    @Test
    void findItemsByNameAndStockTrue_shouldCallRepository() {
        when(itemRepository.findByItemNameContainingIgnoreCaseAndInStockTrue("phone"))
                .thenReturn(List.of(createItem()));
        List<ItemShortDto> result = itemService.findItemsByNameAndStockTrue("phone");
        assertEquals(1, result.size());
        verify(itemRepository).findByItemNameContainingIgnoreCaseAndInStockTrue("phone");
    }

    @Test
    void updateItem_shouldUpdateFields() {
        Item item = createItem();
        when(itemRepository.getReferenceById(1)).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation ->
                        invocation.getArgument(0));
        ItemCreateUpdateDto update =
                ItemCreateUpdateDto.builder()
                        .itemName("Новый телефон")
                        .price(1500F)
                        .inStock(false)
                        .seller("Samsung")
                        .build();

        ItemPageDto result = itemService.updateItem(1, update);
        assertEquals("Новый телефон", result.getItemName());
        assertEquals(1500F, result.getPrice());
        assertFalse(result.getInStock());
        assertEquals("Samsung", result.getSeller());
        verify(itemRepository).save(item);
    }

    @Test
    void updateItem_whenFieldIsNull_shouldKeepOldValue() {
        Item item = createItem();
        when(itemRepository.getReferenceById(1)).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation ->
                invocation.getArgument(0));
        ItemCreateUpdateDto update = ItemCreateUpdateDto.builder()
                        .price(2000F)
                        .build();
        ItemPageDto result = itemService.updateItem(1, update);
        assertEquals("Телефон", result.getItemName());
        assertEquals(2000F, result.getPrice());
        assertEquals("Apple", result.getSeller());
    }

    @Test
    void deleteFromStockItem_shouldSetInStockFalse() {
        Item item = createItem();
        when(itemRepository.getReferenceById(1)).thenReturn(item);
        itemService.deleteFromStockItem(1);
        assertFalse(item.getInStock());
        verify(itemRepository).save(item);
    }
}
