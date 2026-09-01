package com.academy.shop.controller;

import com.academy.shop.dto.item.request.ItemCreateUpdateDto;
import com.academy.shop.dto.item.response.ItemPageDto;
import com.academy.shop.dto.item.response.ItemShortDto;
import com.academy.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping()
    public String getItems(Model model) {
        List<ItemShortDto> itemShortDtos = itemService.findItemsByStock();
        model.addAttribute("itemShortDtos",itemShortDtos);

        return "items";
    }

    @GetMapping("/admin")
    public String getAllItems(Model model) {
        List<ItemShortDto> itemShortDtos = itemService.findAllItems();
        model.addAttribute("itemShortDtos",itemShortDtos);

        return "items";
    }

    @GetMapping("/{itemId}")
    public String getItem(Model model, @PathVariable Integer itemId) {
        ItemPageDto itemPageDto = itemService.findItemById(itemId);
        model.addAttribute("itemPageDto",itemPageDto);

        return "item";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam String itemName) {
        List<ItemShortDto> itemShortDtos = itemService.findItemsByNameAndStockTrue(itemName);
        model.addAttribute("itemShortDtos",itemShortDtos);

        return "items";
    }

    @GetMapping("/admin/advanced-search")
    public String advancedSearch(
            Model model,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) Boolean inStock) {
        List<ItemShortDto> itemShortDtos = itemService.findItemsByNameByStock(itemName, inStock);
        model.addAttribute("itemShortDtos",itemShortDtos);

        return "items";
    }

    @GetMapping("/filter")
    public String filter(
            Model model,
            @RequestParam(required = false)
            String itemName,
            @RequestParam(required = false)
            Boolean inStock,
            @RequestParam(required = false)
            Float minPrice,
            @RequestParam(required = false)
            Float maxPrice,
            @RequestParam(required = false)
            String seller,
            @RequestParam(required = false)
            LocalDate minDate,
            @RequestParam(required = false)
            LocalDate maxDate,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin) {
            inStock = true;
        }

        List<ItemShortDto> itemShortDtos = itemService.findItemsFiltered(
                itemName,
                inStock,
                minPrice,
                maxPrice,
                seller,
                minDate,
                maxDate);
        model.addAttribute("itemShortDtos",itemShortDtos);

        return "items";
    }

    @GetMapping("/admin/create-item")
    public String createItemForm() {
        return "createItem";
    }

    @PostMapping("/admin/create-item")
    public String createItem(
            Model model,
            @RequestParam String itemName,
            @RequestParam Float price,
            @RequestParam Boolean inStock,
            @RequestParam String seller,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate arrivalDate) {
        ItemCreateUpdateDto itemCreateUpdateDto = ItemCreateUpdateDto.builder()
                .itemName(itemName)
                .price(price)
                .inStock(inStock)
                .seller(seller)
                .description(description)
                .arrivalDate(arrivalDate)
                .build();
        ItemPageDto itemPageDto = itemService.createItem(itemCreateUpdateDto);
        Integer itemId = itemPageDto.getItemId();
        model.addAttribute("itemPageDto", itemPageDto);

        return "redirect:/items/" + itemId;
    }

    @GetMapping("/admin/{itemId}/edit-item")
    public String updateItem(Model model, @PathVariable Integer itemId) {
        ItemPageDto itemPageDto = itemService.findItemById(itemId);
        model.addAttribute("itemPageDto", itemPageDto);

        return "editItem";
    }

    @PostMapping("/admin/{itemId}/edit-item")
    public String updateItem(
            Model model,
            @PathVariable Integer itemId,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) String seller,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate arrivalDate) {
        ItemCreateUpdateDto itemCreateUpdateDto = ItemCreateUpdateDto.builder()
                .itemName(itemName)
                .price(price)
                .inStock(inStock)
                .seller(seller)
                .description(description)
                .arrivalDate(arrivalDate)
                .build();
        ItemPageDto itemPageDto = itemService.updateItem(itemId, itemCreateUpdateDto);
        model.addAttribute("itemPageDto", itemPageDto);

        return "redirect:/items/" + itemId;
    }

    @PostMapping("/admin/{itemId}/delete")
    public String deleteItem(@PathVariable Integer itemId) {
        itemService.deleteFromStockItem(itemId);

        return "redirect:/items";
    }
}
