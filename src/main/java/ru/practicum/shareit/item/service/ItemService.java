package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(NewItemDto newItem, Long userId);

    ItemDto update(Long itemId, Long userId, UpdateItemDto updateItem);

    ItemFullInfoDto getById(Long itemId, Long userId);

    List<ItemFullInfoDto> getAll(Long userId);

    void delete(Long itemId);

    List<ItemDto> search(Long userId, String text);

    CommentDto createComment(Long itemId, Long userId, String text);
}
