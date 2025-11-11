package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class GatewayItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;
    private final String headerName = "X-Sharer-User-Id";

    @Test
    void shouldGetItemById() throws Exception {
        Long itemId = 1L;
        Long userId = 1L;
        String expectedResponse = """
                [{
                    "id": 1,
                    "name": "Test Item",
                    "description": "Test Description",
                    "available": true
                }]
                """;

        when(itemClient.getItemById(itemId, userId))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(get("/items/{id}", itemId)
                        .header(headerName, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldGetAllItemsByUserId() throws Exception {
        Long userId = 1L;
        String expectedResponse = """
                [{
                    "id": 1,
                    "name": "Item 1",
                    "available": true
                }, {
                    "id": 2,
                    "name": "Item 2",
                    "available": false
                }]
                """;

        when(itemClient.getAllItems(userId))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(get("/items")
                        .header(headerName, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldPostItem() throws Exception {
        Long userId = 1L;
        NewItemDto newItem = new NewItemDto();
        newItem.setDescription("item description");
        newItem.setName("item name");
        newItem.setAvailable(true);
        newItem.setRequestId(1L);

        String expectedResponse = """
                [{
                    "id": 1,
                    "name": "item name",
                    "description": "item description",
                    "available": true
                }]
                """;

        when(itemClient.createItem(eq(userId), any(NewItemDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(expectedResponse));

        // when & then
        mockMvc.perform(post("/items")
                        .header(headerName, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldPatchItem() throws Exception {
        Long itemId = 1L;
        Long userId = 1L;

        UpdateItemDto itemDto = new UpdateItemDto();
        itemDto.setDescription("Updated Item");
        itemDto.setName("Updated Description");
        itemDto.setAvailable(false);

        String expectedResponse = """
                [{
                    "id": 1,
                    "name": "Updated Item",
                    "description": "Updated Description",
                    "available": false
                }]
                """;

        when(itemClient.updateItem(eq(itemId), eq(userId), any(UpdateItemDto.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(patch("/items/{id}", itemId)
                        .header(headerName, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}
