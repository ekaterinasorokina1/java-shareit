package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public class RequestMapper {
    public static Request mapToRequest(NewRequestDto newRequest, long userId) {
        Request request = new Request();
        request.setDescription(newRequest.getDescription());
        request.setRequestorId(userId);
        return request;
    }

    public static ItemRequestDto mapToRequestDto(Request request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setRequestorId(request.getRequestorId());
        return dto;
    }

    public static ItemRequestWithItemsDto mapToRequestWithItemsDto(Request request, List<ItemForRequestDto> items) {
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setRequestorId(request.getRequestorId());
        dto.setItems(items);
        return dto;
    }
}
