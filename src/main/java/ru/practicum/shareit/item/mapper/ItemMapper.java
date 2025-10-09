package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;


public class ItemMapper {
    public static Item mapToItem(NewItemDto newItem, long userId) {
        Item item = new Item();
        item.setName(newItem.getName());
        item.setDescription(newItem.getDescription());
        item.setAvailable(newItem.getAvailable());
        item.setOwnerId(userId);
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setAvailable(item.getAvailable());
        dto.setDescription(item.getDescription());
        dto.setOwnerId(item.getOwnerId());
        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemDto updateItem) {
        if (updateItem.hasName()) {
            item.setName(updateItem.getName());
        }
        if (updateItem.hasDescription()) {
            item.setDescription(updateItem.getDescription());
        }

        if (updateItem.hasAvailable()) {
            item.setAvailable(updateItem.getAvailable());
        }

        return item;
    }
}
