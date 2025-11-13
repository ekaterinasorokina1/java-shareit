package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class RequestMapper {
    public static Request mapToRequest(NewRequestDto newRequest, User user) {
        Request request = new Request();
        request.setDescription(newRequest.getDescription());
        request.setRequestor(user);
        return request;
    }

    public static ItemRequestDto mapToRequestDto(Request request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setRequestorId(request.getRequestor().getId());
        return dto;
    }

    public static ItemRequestWithItemsDto mapToRequestWithItemsDto(Request request, List<ItemForRequestDto> items) {
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setRequestorId(request.getRequestor().getId());
        dto.setItems(items);
        return dto;
    }
}
