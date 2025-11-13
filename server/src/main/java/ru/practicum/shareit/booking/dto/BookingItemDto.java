package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BookingItemDto {
    private LocalDateTime start;

    private LocalDateTime end;
}
