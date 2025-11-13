package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ItemRequestDto create(Long userId, NewRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        Request request = requestRepository
                .save(RequestMapper.mapToRequest(requestDto, user));
        return RequestMapper.mapToRequestDto(request);
    }

    public ItemRequestWithItemsDto getById(Long userId, Long requestId) {
        checkIfUserExist(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден"));
        List<Item> items = itemRepository.findByRequestId(requestId);
        return RequestMapper.mapToRequestWithItemsDto(request, items.stream().map(ItemMapper::mapToItemForRequestDto).toList());
    }


    public List<ItemRequestDto> getAll(Long userId) {
        checkIfUserExist(userId);
        return requestRepository.findAllByRequestorIdNot(userId).stream().map(RequestMapper::mapToRequestDto).toList();
    }

    public List<ItemRequestWithItemsDto> getUserRequests(Long userId) {
        checkIfUserExist(userId);
        List<Request> requests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream().map(request -> {
            List<Item> items = itemRepository.findByRequestId(request.getId());
            return RequestMapper.mapToRequestWithItemsDto(request, items.stream().map(ItemMapper::mapToItemForRequestDto).toList());
        }).toList();
    }

    private void checkIfUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }
}
