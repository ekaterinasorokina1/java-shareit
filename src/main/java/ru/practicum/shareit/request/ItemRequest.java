package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Integer id;

    private String description;

    private String requestor;

    private LocalDate createdDate;
}
