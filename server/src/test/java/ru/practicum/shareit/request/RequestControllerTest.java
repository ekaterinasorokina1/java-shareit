package ru.practicum.shareit.request;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    private ItemRequestDto getSampleItemRequestDto() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("Request Description");
        dto.setCreated(LocalDateTime.now());
        dto.setRequestorId(100L);
        return dto;
    }

    private NewRequestDto getSampleNewRequestDto() {
        NewRequestDto dto = new NewRequestDto();
        dto.setDescription("New request");
        return dto;
    }

    private ItemRequestWithItemsDto getSampleItemRequestWithItemsDto() {
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto();
        dto.setId(2L);
        dto.setDescription("Request with items");
        dto.setCreated(LocalDateTime.now());
        return dto;
    }

    @Test
    void shouldCreateRequest() throws Exception {
        NewRequestDto newRequest = getSampleNewRequestDto();
        ItemRequestDto savedDto = getSampleItemRequestDto();

        when(requestService.create(anyLong(), any(NewRequestDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedDto.getId()))
                .andExpect(jsonPath("$.description").value(savedDto.getDescription()));

        verify(requestService).create(eq(123L), any(NewRequestDto.class));
    }

    @Test
    void shouldReturnItemById() throws Exception {
        ItemRequestWithItemsDto dto = getSampleItemRequestWithItemsDto();

        when(requestService.getById(anyLong(), eq(5L))).thenReturn(dto);

        mockMvc.perform(get("/requests/5")
                        .header("X-Sharer-User-Id", 456L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
        verify(requestService).getById(eq(456L), eq(5L));
    }

    @Test
    void shouldReturnListOfItem() throws Exception {
        List<ItemRequestDto> list = List.of(
                getSampleItemRequestDto(),
                getSampleItemRequestDto()
        );

        when(requestService.getAll(anyLong())).thenReturn(list);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 789L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(list.get(1).getId()));

        verify(requestService).getAll(eq(789L));
    }

    @Test
    void shouldReturnListOfUserItemRequest() throws Exception {
        List<ItemRequestWithItemsDto> list = List.of(
                getSampleItemRequestWithItemsDto()
        );

        when(requestService.getUserRequests(anyLong())).thenReturn(list);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 321L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.getFirst().getId()));

        verify(requestService).getUserRequests(eq(321L));
    }
}
