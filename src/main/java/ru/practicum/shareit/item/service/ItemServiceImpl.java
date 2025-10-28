package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemFullInfoDto getById(Long itemId, Long userId) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найден"));

        List<CommentDto> comments = commentRepository.findByItemId(item.getId()).stream().map(CommentMapper::mapToCommentDto).toList();

        BookingItemDto lastBookingItem = null;
        BookingItemDto nextBookingItem = null;
        if (item.getOwnerId().equals(userId)) {
            List<Booking> listLastBooking = bookingRepository.findByItemIdAndEndDateBeforeOrderByEndDateDesc(item.getId(), LocalDateTime.now());
            if (!listLastBooking.isEmpty()) {
                lastBookingItem = BookingMapper.mapToBookingItemDto(listLastBooking.getFirst());
            }

            List<Booking> nextBookingList = bookingRepository.findByItemIdAndStartDateAfterOrderByStartDate(item.getId(), LocalDateTime.now());
            if (!nextBookingList.isEmpty()) {
                nextBookingItem = BookingMapper.mapToBookingItemDto(nextBookingList.getFirst());
            }
        }
        return ItemMapper.mapToItemFullInfoDto(item, comments, nextBookingItem, lastBookingItem);
    }

    @Override
    @Transactional
    public ItemDto create(NewItemDto newItem, Long userId) {
        checkIfUserExist(userId);

        Item item = itemRepository.save(ItemMapper.mapToItem(newItem, userId));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(Long itemId, Long userId, UpdateItemDto updateItem) {
        checkIfUserExist(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найден"));
        ItemMapper.updateItemFields(item, updateItem);
        item = itemRepository.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemFullInfoDto> getAll(Long userId) {
        return itemRepository.findByOwnerId(userId).stream().map(item -> {
            List<CommentDto> comments = commentRepository.findByItemId(item.getId()).stream().map(CommentMapper::mapToCommentDto).toList();

            BookingItemDto lastBookingItem = null;
            BookingItemDto nextBookingItem = null;

            List<Booking> listLastBooking = bookingRepository.findByItemIdAndEndDateBeforeOrderByEndDateDesc(item.getId(), LocalDateTime.now());
            if (!listLastBooking.isEmpty()) {
                lastBookingItem = BookingMapper.mapToBookingItemDto(listLastBooking.getFirst());
            }

            List<Booking> nextBookingList = bookingRepository.findByItemIdAndStartDateAfterOrderByStartDate(item.getId(), LocalDateTime.now());
            if (!nextBookingList.isEmpty()) {
                nextBookingItem = BookingMapper.mapToBookingItemDto(nextBookingList.getFirst());
            }
            return ItemMapper.mapToItemFullInfoDto(item, comments, nextBookingItem, lastBookingItem);
        }).toList();
    }

    @Override
    @Transactional
    public void delete(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        return itemRepository.search(userId, text).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    @Transactional
    public CommentDto createComment(Long itemId, Long userId, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найден"));

        if (bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndDateBefore(itemId, userId, StatusEnum.APPROVED, LocalDateTime.now())
                .isEmpty()) {
            throw new ValidationException("Завершенного бронирования не найдено");
        }
        return CommentMapper.mapToCommentDto(commentRepository.save(CommentMapper.mapToComment(item, user, text)));
    }

    private void checkIfUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }
}
