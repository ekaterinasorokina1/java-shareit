package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public class ItemMapper {
    public static Item mapToItem(NewItemDto newItem, User user) {
        Item item = new Item();
        item.setName(newItem.getName());
        item.setDescription(newItem.getDescription());
        item.setAvailable(newItem.getAvailable());
        item.setOwner(user);
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setAvailable(item.getAvailable());
        dto.setDescription(item.getDescription());
        dto.setOwnerId(item.getOwner().getId());
        return dto;
    }

    public static ItemFullInfoDto mapToItemFullInfoDto(Item item, List<CommentDto> comments, BookingItemDto nextBooking, BookingItemDto lastBooking) {
        ItemFullInfoDto dto = new ItemFullInfoDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setAvailable(item.getAvailable());
        dto.setDescription(item.getDescription());
        dto.setOwnerId(item.getOwner().getId());
        dto.setComments(comments);
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemDto updateItem, User user) {
        if (updateItem.hasName()) {
            item.setName(updateItem.getName());
        }
        if (updateItem.hasDescription()) {
            item.setDescription(updateItem.getDescription());
        }

        if (updateItem.hasAvailable()) {
            item.setAvailable(updateItem.getAvailable());
        }

        if (updateItem.hasRequestId()) {
            item.setOwner(user);
        }

        return item;
    }

    public static ItemForRequestDto mapToItemForRequestDto(Item item) {
        ItemForRequestDto dto = new ItemForRequestDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwner().getId());
        return dto;
    }
}
