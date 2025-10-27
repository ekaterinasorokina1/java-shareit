package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewBookingDto {
    private Long itemId;

    @NotNull
    @Future
    private LocalDateTime start;

    @Future
    @NotNull
    private LocalDateTime end;
}
