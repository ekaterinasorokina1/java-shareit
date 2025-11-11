package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Setter
@Getter
public class BookingDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private StatusEnum status;

    private User booker;

    private Item item;
}
