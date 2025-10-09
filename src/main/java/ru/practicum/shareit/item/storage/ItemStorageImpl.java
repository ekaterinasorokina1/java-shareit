package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemStorageImpl implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getById(int itemId) {
        return Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найден"));
    }

    @Override
    public List<Item> getAll(long userId) {
        return items.values().stream().filter(item -> item.getOwnerId() == userId).toList();
    }

    @Override
    public void delete(int itemId) {
        items.remove(itemId);
    }

    @Override
    public List<Item> search(long userId, String text) {
        return getAll(userId).stream()
                .filter(item -> item.getAvailable() && item.getName().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    private int getNextId() {
        int currentMaxId = items.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
