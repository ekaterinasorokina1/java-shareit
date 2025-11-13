package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateEnum;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto create(Long userId, NewBookingDto newBooking) {
        User booker = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Item item = itemRepository
                .findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + newBooking.getItemId() + " не найден"));

        checkIsAvailable(item);
        checkIsEndStartEqual(newBooking.getStart(), newBooking.getEnd());

        return BookingMapper.mapToBookingDto(bookingRepository.save(BookingMapper.mapToBooking(booker, item, StatusEnum.WAITING, newBooking)));
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + bookingId + " не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Изменить статус бронирования может только владелец");
        }
        booking.setStatus(approved ? StatusEnum.APPROVED : StatusEnum.REJECTED);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(Long bookingId, Long userId) {
        checkIsUserExist(userId);

        return BookingMapper.mapToBookingDto(bookingRepository.findByUserIdOrBookerId(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = \" + bookingId + \" не найдено\"")));
    }

    @Override
    public List<BookingDto> getAllUserBooking(Long userId, StateEnum state) {
        checkIsUserExist(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookingList;
        bookingList = switch (state) {
            case CURRENT ->
                    bookingRepository.findByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDate(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndDateBeforeOrderByEndDate(userId, currentTime);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartDateAfterOrderByStartDate(userId, currentTime);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDate(userId, StateEnum.WAITING);
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDate(userId, StateEnum.REJECTED);
            default -> bookingRepository.findByItemOwnerIdOrderByStartDate(userId);
        };

        return bookingList.stream().map(BookingMapper::mapToBookingDto).toList();
    }

    @Override
    public List<BookingDto> getAllBookerBooking(Long bookerId, StateEnum state) {
        checkIsUserExist(bookerId);
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookingList;
        bookingList = switch (state) {
            case CURRENT ->
                    bookingRepository.findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDate(bookerId, currentTime, currentTime);
            case PAST -> bookingRepository.findByBookerIdAndEndDateBeforeOrderByEndDate(bookerId, currentTime);
            case FUTURE -> bookingRepository.findByBookerIdAndStartDateAfterOrderByStartDate(bookerId, currentTime);
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDate(bookerId, StateEnum.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDate(bookerId, StateEnum.REJECTED);
            default -> bookingRepository.findByBookerIdOrderByStartDate(bookerId);
        };

        return bookingList.stream().map(BookingMapper::mapToBookingDto).toList();
    }

    private void checkIsUserExist(Long userId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private void checkIsEndStartEqual(LocalDateTime start, LocalDateTime end) {
        if (start.isEqual(end)) {
            throw new ValidationException("Даты не должны быть равны");
        }
    }

    private void checkIsAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Бронирование не доступно");
        }
    }
}

