package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto getById(int itemId) {
        return ItemMapper.mapToItemDto(itemStorage.getById(itemId));
    }

    @Override
    public ItemDto create(NewItemDto newItem, long userId) {
        userStorage.getById((int) userId);
        Item item = ItemMapper.mapToItem(newItem, userId);
        item = itemStorage.create(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto update(int itemId, long userId, UpdateItemDto updateItem) {
        userStorage.getById((int) userId);
        Item item = itemStorage.getById(itemId);
        ItemMapper.updateItemFields(item, updateItem);
        item = itemStorage.update(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemStorage.getAll(userId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    public void delete(int itemId) {
        itemStorage.delete(itemId);
    }

    @Override
    public List<ItemDto> search(long userId, String text) {
        return itemStorage.search(userId, text).stream().map(ItemMapper::mapToItemDto).toList();
    }
}
