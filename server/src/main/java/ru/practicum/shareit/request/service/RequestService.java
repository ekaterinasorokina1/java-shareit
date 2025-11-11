package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto create(Long userId, NewRequestDto requestDto);

    ItemRequestWithItemsDto getById(Long userId, Long requestId);

    List<ItemRequestDto> getAll(Long userId);

    List<ItemRequestWithItemsDto> getUserRequests(Long userId);
}
