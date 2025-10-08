package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(NewItemDto newItem, long userId);

    ItemDto update(int itemId, long userId, UpdateItemDto updateItem);

    ItemDto getById(int itemId);

    List<ItemDto> getAll(long userId);

    void delete(int itemId);

    List<ItemDto> search(long userId, String text);
}
