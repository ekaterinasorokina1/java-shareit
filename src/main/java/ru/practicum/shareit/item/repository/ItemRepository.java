package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query(" select i from Item i " +
            "where i.available = true and i.ownerId = ?1 and upper(i.name) like upper(concat('%', ?2, '%')) " +
            " or upper(i.description) like upper(concat('%', ?2, '%'))")
    List<Item> search(Long userId, String text);
}
