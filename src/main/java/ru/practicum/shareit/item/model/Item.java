package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class Item {
    private Integer id;

    @NotNull
    private String name;

    private String description;

    private Boolean available;

    @NotNull
    private Long ownerId;

    private ItemRequest request;
}
