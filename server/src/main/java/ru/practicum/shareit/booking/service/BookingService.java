package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.StateEnum;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, NewBookingDto newBooking);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getAllUserBooking(Long userId, StateEnum state);

    List<BookingDto> getAllBookerBooking(Long userId, StateEnum state);
}
