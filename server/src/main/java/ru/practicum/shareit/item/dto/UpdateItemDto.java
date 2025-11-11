package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class UpdateItemDto {
    private String name;

    private String description;

    private Boolean available;

    private int ownerId;

    private Long requestId;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasAvailable() {
        return available != null;
    }

    public boolean hasRequestId() {
        return requestId != null;
    }
}
