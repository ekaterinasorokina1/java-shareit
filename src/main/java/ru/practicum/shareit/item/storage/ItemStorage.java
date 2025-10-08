package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    Item update(Item item);

    Item getById(int itemId);

    List<Item> getAll(long userId);

    void delete(int itemId);

    List<Item> search(long userId, String text);
}
