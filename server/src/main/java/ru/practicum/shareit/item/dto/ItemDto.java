package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private Long id;

    private String name;

    private String description;

    private boolean available;

    private Long ownerId;

    private List<CommentDto> comments;
}
